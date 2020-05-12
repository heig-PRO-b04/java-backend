import api

reg = register("testModifiedAnswer", "1234")

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Test de poll"
poll = postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

q1 = postQuestion(tokenMod, idMod, idPoll, "Question 1", "visible", 1, 0, 0)
idQ1 = q1["idQuestion"]

ans11_Title = "haha"
ans12_Title = "Réponse 2"
postAnswer(tokenMod, idMod, idPoll, idQ1, ans11_Title, "")
postAnswer(tokenMod, idMod, idPoll, idQ1, ans12_Title, "")

q2 = postQuestion(tokenMod, idMod, idPoll, "Question 2", "visible", 1, 0, 0)
idQ2 = q2["idQuestion"]
ans21_Title = "Clarisse"
ans22_Title = "David"
ans23_Title = "Alexandre"
ans24_Title = "Matthieu"
ans25_Title = "Guy-Laurent"
postAnswer(tokenMod, idMod, idPoll, idQ2, ans21_Title, "")
postAnswer(tokenMod, idMod, idPoll, idQ2, ans22_Title, "")
postAnswer(tokenMod, idMod, idPoll, idQ2, ans23_Title, "")
postAnswer(tokenMod, idMod, idPoll, idQ2, ans24_Title, "")
postAnswer(tokenMod, idMod, idPoll, idQ2, ans25_Title, "")

code = putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = connectUser(code)["token"]

session = getSession(tokenUser)
user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]

user_questions = getQuestion(tokenUser, user_idMod, user_idPoll)
for question in user_questions:
    if (question["title"] != q1["title"]) and (question["title"] != q2["title"]):
        sys.exit(1)
