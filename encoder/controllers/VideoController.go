package controllers

import (
	"fmt"
	"github.com/gin-gonic/gin"
	_ "github.com/giorgisio/goav/avformat"
	"net/http"
)

func SayVideo() {
	fmt.Println("videoController")
}

func GetVideos(c *gin.Context) {

	c.JSON(http.StatusOK, gin.H{"data": "ok"})
}