import api
import sys

reg = api.register("testModifiedAnswer", "1234")

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Test de poll"
poll = api.postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

q1 = api.postQuestion(tokenMod, idMod, idPoll, "Question 1", "visible", 1, 0, 0)
idQ1 = q1["idQuestion"]

ans1 = [ "haha"
       , "Réponse 2" ]
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans1[0], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans1[1], "")

q2 = api.postQuestion(tokenMod, idMod, idPoll, "Question 2", "visible", 1, 0, 0)
idQ2 = q2["idQuestion"]
ans2 = [ "Clarisse"
       , "David"
       , "Alexandre"
       , "Matthieu"
       , "Guy-Laurent" ]
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans2[0], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans2[1], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans2[2], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans2[3], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans2[4], "")

code = api.putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = api.connectUser(code)["token"]

session = api.getSession(tokenUser)
user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]

user_questions = api.getQuestion(tokenUser, user_idMod, user_idPoll)

flag = False;
for question in user_questions:
    if (question["title"] != q1["title"]) and (question["title"] != q2["title"]):
        sys.exit(1)

    answers = api.getAnswer(tokenUser, user_idMod, user_idPoll, question["idQuestion"])
    for ans in answers:
        if ans["title"] in (ans1 + ans2):
            flag = True

if not flag:
    print("[FAILED] Title from answers should be found in answer list")
    sys.exit(1)
else:
    sys.exit(0)
