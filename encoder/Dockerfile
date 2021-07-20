FROM golang:1.13.8

WORKDIR /

COPY . .

RUN go get -d -v ./...

RUN go install -v ./...

EXPOSE 8081

CMD ["main"]