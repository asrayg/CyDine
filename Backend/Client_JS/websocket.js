var ws;

function connect() {
    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;
    var url = wsserver + username;
    //var url = "ws://echo.websocket.org";

    ws = new WebSocket(url);

    ws.onmessage = function(event) {
        const log = document.getElementById("log");

        if (typeof event.data === 'string') {
            // Handle text messages and append them line by line
            const messageElement = document.createElement('p');
            messageElement.textContent = event.data;
            log.appendChild(messageElement);
            log.scrollTop = log.scrollHeight;
        } else if (event.data instanceof Blob) {
            // Handle image data and display it in chat log
            const reader = new FileReader();
            reader.onload = function() {
                const img = document.createElement("img");
                img.src = reader.result;
                log.appendChild(img);
                log.scrollTop = log.scrollHeight;
            };
            reader.readAsDataURL(event.data);
        }
    };

    ws.onopen = function(event) { // called when connection is opened
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "<br>"; // Changed to <br>
    };
}

function send() {  // this is how to send messages
    var content = document.getElementById("msg").value;
    ws.send(content);
}

function uploadImage() {
    const fileInput = document.getElementById('imageUpload');
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select an image before uploading.");
        return;
    }

    const reader = new FileReader();

    reader.onloadend = function(e) {
        const arrayBuffer = e.target.result;
        ws.send(arrayBuffer);
    };

    reader.readAsArrayBuffer(file);
}



