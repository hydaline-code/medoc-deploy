var extraImagesCount = 0;

$(document).ready(function() {	
	// Initialize rich text editor
	$(document).ready(function () {
	    $("#shortDescription").richText();

	    setTimeout(function () {
	        let content = $("#shortDescription").val().trim();
	        console.log("Valeur réelle :", content);

	        if (content !== "" && content !== "<div><br></div>") {
	            $(".richText-editor").attr("contenteditable", "false");
	            $(".richText-toolbar").hide();
	        } else {
	            $(".richText-editor").attr("contenteditable", "true");
	            $(".richText-toolbar").show();
	        }
	    }, 500);
	});

	// Main image preview
	$("#fileImage").change(function() {
		if (!checkFileSize(this)) {
			return;
		}
		showImageThumbnail(this);
	});

	// Extra images preview
	$("input[name='extraImage']").each(function(index) {
		extraImagesCount++;
		
		$(this).change(function() {
			if (!checkFileSize(this)) {
				return;
			}			
			showExtraImageThumbnail(this, index);
		});
	});
	
	// Remove extra image buttons
	$("button[name='linkRemoveExtraImage']").each(function(index) {
		$(this).click(function(e) {
			e.preventDefault();
			removeExtraImage(index);
		});
	});
	
});

// Main image preview function
function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	
	if (file) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbnail").attr("src", e.target.result);
			$("#thumbnail").css("display", "block");
			$("#thumbnail").parent().find(".image-placeholder").hide();
			$("#thumbnail").closest(".image-upload-card").addClass("has-image");
		};
		reader.readAsDataURL(file);
	}
}

function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	
	if (file) {
		fileName = file.name;
		
		imageNameHiddenField = $("#imageName" + index);
		if (imageNameHiddenField.length) {
			imageNameHiddenField.val(fileName);
		}
		
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#extraThumbnail" + index).attr("src", e.target.result);
			$("#extraThumbnail" + index).css("display", "block");
			$("#extraThumbnail" + index).parent().find(".image-placeholder").hide();
			$("#divExtraImage" + index).addClass("has-image");
		};
		
		reader.readAsDataURL(file);	
		
		if (index >= extraImagesCount - 1) {
			addNextExtraImageSection(index + 1);		
		}
	}
}

function addNextExtraImageSection(index) {
	htmlExtraImage = `
		<div class="image-upload-card" id="divExtraImage${index}">
			<label class="image-label" id="extraImageHeader${index}">
				<i class="fas fa-plus-circle"></i>
				Image supplémentaire #${index + 1}
			</label>
			<div class="image-preview-container">
				<i class="fas fa-image image-placeholder"></i>
				<img id="extraThumbnail${index}" 
				     alt="Extra image #${index + 1} preview" 
				     class="image-preview"
				     src="${defaultImageThumbnailSrc}"
				     style="display: none;" />
			</div>
			<div class="file-input-wrapper">
				<input type="file" 
				       name="extraImage"
				       class="file-input"
				       onchange="showExtraImageThumbnail(this, ${index})" 
				       accept="image/png, image/jpeg" />
			</div>
		</div>	
	`;
	
	htmlLinkRemove = `
		<button type="button" 
		        class="btn-remove-image"
		        onclick="removeExtraImage(${index - 1})" 
		        title="Supprimer cette image">
			<i class="fas fa-times"></i>
		</button>
	`;
	
	$(".images-grid").append(htmlExtraImage);
	
	$("#divExtraImage" + (index - 1)).prepend(htmlLinkRemove);
	
	extraImagesCount++;
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
}