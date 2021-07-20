from flask import Flask, request
import os

app = Flask(__name__)


@app.route('/video/<code>/<mail>', methods=['POST'])
def video(code, mail):
    if request.method == "POST":
        if code == "END":
            os.system(
                'echo "Hi !\nWe are happy to inform you that your video is nom fully available on our website." | mail -s "Your video is fully uploaded" ' + mail)
        return {
            'message': 'OK'
        }


@app.route('/password/<code>/<mail>', methods=['POST'])
def video(code, mail):
    if request.method == "POST":
        if code == "CHANGED":
            os.system(
                'echo "Hi !\nWe are happy to inform you that your password got successfully changed." | mail -s "Your password has been changed." ' + mail)
        return {
            'message': 'OK'
        }


if __name__ == '__main__':
    app.run(debug=True)
