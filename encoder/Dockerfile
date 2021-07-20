FROM golang:1.13.8

WORKDIR /

COPY go.mod .
COPY go.sum .

RUN go mod download

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y ffmpeg

COPY . .

RUN go build -o ./main-app .

EXPOSE 8081

CMD [ "./main-app" ]
