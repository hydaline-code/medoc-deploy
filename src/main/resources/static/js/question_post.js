$(document).ready(function() {
	$("#buttonPostQuestion").on("click", function(e) {
		e.preventDefault();
		form = $("#formQuestion")[0];
		if (form.checkValidity()) {
			postQuestion();
		} else {
			form.reportValidity();
		}
	});

});


function postQuestion() {
	url = contextPath + "post_question/" + productId;
	question = $("#question").val();

	jsonData = { questionContent: question };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(response) {
		// Clear the question input
		$("#question").val("");

		// Insert the new question into the questions list (without a full reload)
		try {
			var data = response;
			// If server returned wrapped JSON under 'body' or similar, normalize
			if (data && data.hasOwnProperty('id') === false && data.hasOwnProperty('questionContent') === false) {
				// try to extract from data.body
				data = data.body || data;
			}

			var asker = data.asker || 'You';
			var questionHtml = '\n                <div class="question-item" data-question-id="' + data.id + '">\n                    <div class="question-header">\n                        <div class="question-badge">\n                            <i class="fas fa-question-circle"></i>\n                            <span>Question</span>\n                        </div>\n                        <div class="question-meta">\n                            <span class="question-author">\n                                <i class="fas fa-user"></i>\n                                ' + $('<div>').text(asker).html() + '\n                            </span>\n                            <span class="question-time">\n                                <i class="fas fa-clock"></i>\n                                <span>' + (new Date(data.askTime)).toLocaleString() + '</span>\n                            </span>\n                        </div>\n                    </div>\n                    <div class="question-content">' + $('<div>').text(data.questionContent).html() + '</div>\n                    <div class="unanswered-section">\n                        <i class="fas fa-hourglass-half"></i>\n                        <span>Awaiting answer...</span>\n                    </div>\n                </div>\n            ';

			var $list = $(".questions-list-content");
			if ($list.length) {
				$list.prepend(questionHtml);
			}

			// Update question counts in the stats (first .stat-number is total)
			var $statNumbers = $(".questions-stats .stat-number");
			if ($statNumbers.length >= 1) {
				var total = parseInt($statNumbers.eq(0).text() || 0, 10);
				$statNumbers.eq(0).text(total + 1);
			}
		} catch (err) {
			console.error('Failed to insert question into DOM', err);
		}

		// Show success message
		showModalDialog("Post Question", "Your question has been posted and awaiting for approval.");
		// Do not reload; keep modal for confirmation only
	}).fail(function(xhr, status, error) {
		console.error("Error posting question:", error, xhr.responseText);
		showErrorModal("Failed to submit question. May be you have not logged in or session timed out. Try to login and post question again.");
	});
}