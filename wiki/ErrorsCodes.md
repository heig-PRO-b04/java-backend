# HTTP Error codes
For this project, there is a certain number of request that can be sent from the android client to the backend. Here is a list of certain http response codes that can be received in certain cases :

| Code | Detail(s) |
|:----:|--------------|
|200   | The request is correct and the response was sent |
|400   | Bad request, can happen when a client enters a wrong emoji code for eemple |
|403   | Forbidden action, the request was correct but given with the wrong token value |
|405   | Method not allowed, can happen if the client tries to connect to a session without a token |
