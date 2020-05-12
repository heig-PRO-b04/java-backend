#!/bin/env python3

import api
import sys

username="moderator"
password="1234"

token1 = api.register(username, password)
token2 = api.auth(username, password)

if token1 != token2:
    sys.exit(1)
else:
    sys.exit(0)
