var ws;
var displayedMessages = new Set(); // To keep track of displayed messages

function connect() {
    // Close existing connection if it's open
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
    }

    var username = document.getElementById("username").value;
    var wsserver = document.getElementById("wsserver").value;
    var url = wsserver + username;

    ws = new WebSocket(url);

    ws.onmessage = function(event) {
        const log = document.getElementById("log");
        const message = event.data;

        // Check if the message has already been displayed
        if (!displayedMessages.has(message)) {
            if (typeof message === 'string') {
                // Handle text messages and append them line by line
                const messageElement = document.createElement('p');
                messageElement.textContent = message;
                log.appendChild(messageElement);
            } else if (message instanceof Blob) {
                // Handle image data and display it in chat log
                const reader = new FileReader();
                reader.onload = function() {
                    const img = document.createElement("img");
                    img.src = reader.result;
                    log.appendChild(img);
                };
                reader.readAsDataURL(message);
            }
            log.scrollTop = log.scrollHeight;

            // Add the message to the set of displayed messages
            displayedMessages.add(message);
        }
    };

    ws.onopen = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Connected to " + event.currentTarget.url + "<br>";
    };

    ws.onclose = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Disconnected from server<br>";
    };

    ws.onerror = function(event) {
        var log = document.getElementById("log");
        log.innerHTML += "Error: " + event.message + "<br>";
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



