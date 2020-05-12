#!/bin/env python3
import requests
import json

server="http://localhost:80"

def start():
    res = requests.get(server)
    return res.status_code

status = 400

while (status != 200):
    status = start()
