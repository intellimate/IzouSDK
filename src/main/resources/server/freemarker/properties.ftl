<!DOCTYPE html>
<html lang="en" style="height: 100%;">
<head>
    <meta charset="UTF-8">
    <title>${app_name} Properties</title>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/showdown/1.4.1/showdown.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function () {
            $.ajaxSetup({
                        'headers':{
                            'Authorization':'Bearer ${token}'
                        }
                    }
            );
            generateMarkdown();
            loadPropertyFile();
        });

        function generateMarkdown(data) {
            var target = document.getElementById('description');
            var converter = new showdown.Converter();
            target.innerHTML = converter.makeHtml(data);
        }

        function loadPropertyFile() {
            $("#property").load("${url_properties}", function(responseTxt, statusTxt, xhr){
                if(statusTxt == "success"){
                    $("#errorResponse").hide();
                }
                if(statusTxt == "error") {
                    handleError(xhr);
                }
            });
        }

        function loadDescription() {
            $.ajax({
                url : '${url_description}',
                data : data,
                type : 'GET',
                contentType : 'text/plain',
                success: function(data) {
                    generateMarkdown(data)
                },
                error: function(xhr) {
                    handleError(xhr);
                }
            });
        }

        function savePropertyFile() {
            var data = $("#property").val();
            $.ajax({
                url : '${url_properties}',
                data : data,
                type : 'PATCH',
                contentType : 'text/plain',
                success: function() {
                        $("#errorResponse").hide();
                },
                error: function(xhr) {
                        handleError(xhr);
                }
            });
        }

        function handleError(xhr) {
            $("#errorResponse").show();
            if (xhr.status != 400) {
                $("#errorResponse").text("An unexpected Error occured: " + xhr.status + ": " + xhr.statusText)
            } else {
                var errorResponse = JSON.parse(responseTxt);
                $("#errorResponse").text("Unable to save the input. Server returned: " + errorResponse.detail);
            }
        }
    </script>
</head>

<body style="height: 100%;">
    <div style="display: none">
        <p id="sourceDesc">${description}</p>
    </div>
    <div style="width:800px; margin:0 auto; height: 100%;">
        <div id="description"></div>
        <div><p id="errorResponse" style="display: none; color: Red; font-weight: bold;">Error</p></div>
        <textarea id="property" style="resize: none; width: 100%; height: 80%;">
        </textarea>
        <button type="button" onclick="savePropertyFile()">Save</button>
    </div>
</body>
</html>