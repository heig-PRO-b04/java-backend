import api
import sys

reg = api.register("aloy", "chieftain")

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Your favorites Togruta and Twi'lek"
poll = api.postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

q1 = api.postQuestion(tokenMod, idMod, idPoll
,"And who would you choose for a torrid night on the beaches of Scarif ?"
 , "visible", 1, 0, 0)
idQ1 = q1["idQuestion"]

ans = [ "Shaak Ti"
       , "Aayla Secura"
       ,"Ahsoka Tano"
       ,"Dark Talon" ]
idA1=ans[0]["idAnswer"]["idAnswer"]
idA2=ans[1]["idAnswer"]["idAnswer"]

api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans[0], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans[1], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans[2], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ1, ans[3], "")

q1=api.putQuestionWithId(tokenMod, idMod, idPoll,idQ1
,"Who would you rather spend a candlelit dinner with on Canto Byte ?"
,"visible", 1,1,1)

q2 = api.postQuestion(tokenMod, idMod, idPoll
, "And who would you choose for a torrid night on the beaches of Scarif ?"
, "visible", 1, 1, 1)
idQ2 = q2["idQuestion"]

api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans[0], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans[1], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans[2], "")
api.postAnswer(tokenMod, idMod, idPoll, idQ2, ans[3], "")

code = api.putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = api.connectUser(code)["token"]
tokenUser2=api.connectUser(code)["token"]

session = api.getSession(tokenUser)

user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]
user2_idMod=user_idMod
user2_idPoll=user_idPoll

api.vote(tokenUser, user_idMod, user_idPoll, idQ1, idA1, True)
api.vote(tokenUser2, user2_idMod, user2_idPoll, idQ2, idA1, True)

user_questions = api.getQuestion(tokenUser, user_idMod, user_idPoll)
stats=api.getStats(tokenMod,idMod,idPoll)

flag = False;
for each in stats:
    for each2 in (each["answers"]):
        if((each2["title"]==ans[0]) and (each2["positive"]==1)):
            flag=True

for question in user_questions:
    if (question["title"] != q1["title"]) and (question["title"] != q2["title"]):
        sys.exit(1)

    answers = api.getAnswer(tokenUser, user_idMod, user_idPoll, question["idQuestion"])
    #for a in answers:
        #if a["title"] in ans:
            #flag = True

if not flag:
    print("[FAILED] Title from answers should be found in answer list")
    sys.exit(1)
else:
    sys.exit(0)
