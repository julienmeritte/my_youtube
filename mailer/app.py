from flask import Flask, request
from flask_restx import reqparse
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import os

app = Flask(__name__)


@app.route('/video', methods=['POST'])
def video():
    if request.method == "POST":
        parser = reqparse.RequestParser()
        parser.add_argument('code')
        parser.add_argument('mail')
        args = parser.parse_args()
        code = args['code']
        to_mail = args['mail']
        if code == "END":
            message = MIMEMultipart()
            from_mail = "amneos.myyt@gmail.com"
            message['From'] = from_mail
            message['To'] = to_mail
            message['Subject'] = "Your video is fully uploaded"
            content = "Hi,\n\nWe are happy to inform you that your last video is now fully available on our website.\n\nCongratulations."
            text = MIMEText(content, 'plain')
            message.attach(text)
            s = smtplib.SMTP('localhost')
            s.sendmail(from_mail, to_mail, message.as_string())
            s.quit()
        return {
            'message': 'OK'
        }


@app.route('/password', methods=['POST'])
def password():
    if request.method == "POST":
        parser = reqparse.RequestParser()
        parser.add_argument('code')
        parser.add_argument('mail')
        args = parser.parse_args()
        code = args['code']
        to_mail = args['mail']
        if code == "CHANGED":
            message = MIMEMultipart()
            from_mail = "amneos.myyt@gmail.com"
            message['From'] = from_mail
            message['To'] = to_mail
            message['Subject'] = "Your password has been changed"
            content = "Hi,\n\nWe are happy to inform you that your password got successfully changed."
            text = MIMEText(content, 'plain')
            message.attach(text)
            s = smtplib.SMTP('localhost')
            s.sendmail(from_mail, to_mail, message.as_string())
            s.quit()
        return {
            'message': 'OK'
        }


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8082)
