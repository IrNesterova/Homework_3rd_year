<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="/css/chat.css" />
</head>
<body>
<noscript>
    <h2>Sorry! Your browser doesn't support Javascript</h2>
</noscript>
<div id="username-page">
    <div class="username-page-container">
        <h1 class="title">Type your username and password</h1>
        <form id="usernameForm" name="usernameForm">
            <div class="form-group">
                <input type="text" id="name" name="sender" placeholder="Username" autocomplete="off" class="form-control"/>
                <br>
                <input type="password" id="password" name="sender_password" placeholder="Password" autocomplete="off" class="form-control"/>
            </div>
            <div class="form-group">
                <button type="submit" class="accent username-submit">Start Chatting</button>
            </div>
        </form>
    </div>
</div>
<div id="chat-page" class="hidden">
    <div class="chat-container">
        <div class="connecting">
            Connecting...
        </div>
        <ul id="messageArea">
            <#if messages??>
                <#list messages as message>
                    <div>
                        ${message.sender}: ${message.content}
                    </div>
                </#list>
            </#if>
        </ul>
        <br>
        <br>
        <form id="messageForm" name="messageForm">
            <div class="form-group">
                <div class="input-group clearfix">
                    <input type="text" id="message" name="message" placeholder="Type a message..." autocomplete="off" class="form-control"/>
                    <button type="submit" class="primary">Send</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.4/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="/js/chat.js"></script>
</body>
</html>