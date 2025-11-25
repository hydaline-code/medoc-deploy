
$(document).ready(function() {
    // Sélectionne tous les boutons avec un ID spécifique pour chaque question
    $("button[id^='buttonPostQuestionAnswer_']").on("click", function(e) {
        e.preventDefault();

        // Récupère l'ID de la question à partir du bouton cliqué
        var questionId = $(this).attr("id").split('_')[1];
//alert(questionId)
        // Cibler le formulaire correspondant à la question cliquée
        var form = $("#formQuestionAnswer_" + questionId)[0];
		postQuestionAnswer(questionId);
        // Si le formulaire est valide, on appelle la fonction pour soumettre la réponse
       
    });
});


function postQuestionAnswer(questionId) {
    // Créer l'URL dynamique pour chaque question
    var url = contextPath + "postQuestionAnswer/" + questionId;
    
    // Récupérer la valeur du textarea spécifique à cette question
    var question = $("#questionAnswer" + questionId).val();

    // Créer les données JSON à envoyer
    var jsonData = { answer: question, id:questionId };

    // Effectuer l'appel AJAX pour soumettre la question
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue); // Ajout du token CSRF
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(response) {
        // On success: clear the textarea immediately and show confirmation modal
        $("#questionAnswer" + questionId).val(""); // clear the specific textarea
        try {
            var data = response;
            if (data && data.hasOwnProperty('answer') === false) {
                data = data.body || data;
            }

            // Find the question item in DOM
            var $qItem = $(".question-item[data-question-id='" + questionId + "']");
            if ($qItem.length) {
                // Remove unanswered section if present
                $qItem.find('.unanswered-section').remove();

                // Append answer section
                var answerer = data.answerer || 'Pharmacy';
                var answerTime = data.answerTime ? new Date(data.answerTime).toLocaleString() : new Date().toLocaleString();
                var answerHtml = '\n                    <div class="answer-section">\n                        <div class="answer-header">\n                            <div class="answer-badge">\n                                <i class="fas fa-check-circle"></i>\n                                <span>Answer</span>\n                            </div>\n                            <div class="answer-meta">\n                                <span class="answer-author">\n                                    <i class="fas fa-user-md"></i>\n                                    ' + $('<div>').text(answerer).html() + '\n                                </span>\n                                <span class="answer-time">\n                                    <i class="fas fa-calendar-check"></i>\n                                    <span>' + answerTime + '</span>\n                                </span>\n                            </div>\n                        </div>\n                        <div class="answer-content">' + $('<div>').text(data.answer).html() + '</div>\n                    </div>\n                ';

                $qItem.append(answerHtml);

                // Update answered count in stats
                var $statNumbers = $(".questions-stats .stat-number");
                if ($statNumbers.length >= 2) {
                    var answered = parseInt($statNumbers.eq(1).text() || 0, 10);
                    $statNumbers.eq(1).text(answered + 1);
                }
            }

        } catch (err) {
            console.error('Failed to update DOM after posting answer', err);
        }

        showModalDialog("Post Answer", "Your answer has been posted successfully.");
    }).fail(function() {
        // En cas d'échec, afficher un message d'erreur
        showErrorModal("Failed to submit the answer. You may not be logged in or your session expired. Please login and try again.");
    });
}
