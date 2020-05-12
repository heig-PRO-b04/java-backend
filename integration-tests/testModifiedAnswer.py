import api

reg = api.register("testModifiedAnswer", "1234")

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Test de poll"
poll = api.postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

q1 = api.postQuestion(tokenMod, idMod, idPoll, "Question 1", "visible", 1, 0, 0)
idQ1 = q1["idQuestion"]

ans11_Title = "haha"
ans12_Title = "Réponse 2"
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans11_Title, "")
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans12_Title, "")

q2 = api.postQuestion(tokenMod, idMod, idPoll, "Question 2", "visible", 1, 0, 0)
idQ2 = q2["idQuestion"]
ans21_Title = "Clarisse"
ans22_Title = "David"
ans23_Title = "Alexandre"
ans24_Title = "Matthieu"
ans25_Title = "Guy-Laurent"
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans21_Title, "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans22_Title, "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans23_Title, "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans24_Title, "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans25_Title, "")

code = api.putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = api.connectUser(code)["token"]

session = api.getSession(tokenUser)
user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]

user_questions = api.getQuestion(tokenUser, user_idMod, user_idPoll)
for question in user_questions:
    if (question["title"] != q1["title"]) and (question["title"] != q2["title"]):
        sys.exit(1)
