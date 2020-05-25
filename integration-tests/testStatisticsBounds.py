#!/bin/env python3

import api
import sys

username="testStatisticsBounds"
password="1234"

moderator = api.register(username, password)
moderatorToken = moderator["token"]
moderatorId = moderator["idModerator"]

poll = api.postPoll(moderatorToken, moderatorId, "My poll")
pollId = poll["idPoll"]

question = api.postQuestion(
    moderatorToken, moderatorId, pollId, "Question", "visible", 1, 1, 1
)
questionId = question["idQuestion"]

# Insert two different answers in the poll. At least one must be selected, at
# most one.
answer1 = api.postAnswer(
    moderatorToken, moderatorId, pollId, questionId, "A1", ""
)
answer2 = api.postAnswer(
    moderatorToken, moderatorId, pollId, questionId, "A2", ""
)

# Open the poll
code = api.putSessionWithId(moderatorToken, moderatorId, pollId, "open")["code"]

# Connect with a user
participantToken = api.connectUser(code)["token"]

# Ensure there are no votes now
votes = api.statistics_poll(moderatorToken, moderatorId, pollId)
if votes[0]["answers"][0]["positive"] != 0:
  print("[FAILED] No votes yet")
  sys.exit(1)
if votes[0]["answers"][1]["positive"] != 0:
  print("[FAILED] No votes yet")
  sys.exit(1)

# Let the user vote once
api.vote(participantToken, moderatorId, pollId, questionId,
         answer1["idAnswer"], True)

# Ensure there is exactly one vote now.
votes = api.statistics_poll(moderatorToken, moderatorId, pollId)
if votes[0]["answers"][0]["positive"] + votes[0]["answers"][1]["positive"] != 1:
  print("[FAILED] Vote note accounted for when it should be")
  sys.exit(1)

# Let the user vote again for the same answer
api.vote(participantToken, moderatorId, pollId, questionId,
         answer1["idAnswer"], True)

# Ensure there is exactly one vote now.
votes = api.statistics_poll(moderatorToken, moderatorId, pollId)
if votes[0]["answers"][0]["positive"] + votes[0]["answers"][1]["positive"] != 1:
  print("[FAILED] Vote note accounted for when it should be")
  sys.exit(1)

# Let the user vote again for a different answer
api.vote(participantToken, moderatorId, pollId, questionId,
         answer2["idAnswer"], True)

# Ensure there are no votes now
votes = api.statistics_poll(moderatorToken, moderatorId, pollId)
if votes[0]["answers"][0]["positive"] != 0:
  print("[FAILED] No votes yet")
  sys.exit(1)
if votes[0]["answers"][1]["positive"] != 0:
  print("[FAILED] No votes yet")
  sys.exit(1)

# Let the user unvote his first answer
api.vote(participantToken, moderatorId, pollId, questionId,
         answer1["idAnswer"], False)

# Ensure there is exactly one vote now.
votes = api.statistics_poll(moderatorToken, moderatorId, pollId)
if votes[0]["answers"][0]["positive"] + votes[0]["answers"][1]["positive"] != 1:
  print("[FAILED] Vote note accounted for when it should be")
  sys.exit(1)