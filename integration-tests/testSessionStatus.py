import api
import sys

password="12345"
reg = api.register("testSessionStatus", password)

tokenMod = reg["token"]
idMod = reg["idModerator"]

pollTitle = "Test de poll"
poll = api.postPoll(tokenMod, idMod, pollTitle)
idPoll = poll["idPoll"]

code = api.putSessionWithId(tokenMod, idMod, idPoll, "open")["code"]

# Switch to user view

tokenUser = api.connectUser(code)["token"]

session = api.getSession(tokenUser)
user_idMod = session["idModerator"]
user_idPoll = session["idPoll"]

user_poll = api.getPollWithId(tokenUser, user_idMod, user_idPoll)

if user_poll != poll:
    print("[FAILED] The polls are not similar")
    sys.exit(1)

api.putSessionWithId(tokenMod, idMod, idPoll, "quarantined")

user_poll2 = api.getPollWithId(tokenUser, user_idMod, user_idPoll)

if user_poll != poll:
    print("[FAILED] The polls are not similar")
    sys.exit(1)

error = api.connectUser(code)["error"]
if error != 403:
    print("[FAILED] Should have received a 403 error")
    sys.exit(1)

api.putSessionWithId(tokenMod, idMod, idPoll, "closed")

user_poll3 = api.getPollWithId(tokenUser, user_idMod, user_idPoll)

if user_poll3["error"] != 403:
    print("[FAILED] Should have received a 403 error")
    sys.exit(1)

api.account_delete(tokenMod, idMod, password)
