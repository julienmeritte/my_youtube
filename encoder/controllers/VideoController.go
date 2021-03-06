package controllers

import (
	"amneos/services"
	"bytes"
	"fmt"
	"github.com/gin-gonic/gin"
	"io/ioutil"
	"log"
	"mime/multipart"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
	"strings"
)

func SayVideo() {
	fmt.Println("VideoController -> SayVideo()")
}

func GetVideos(c *gin.Context) {


	file, err := c.FormFile("file")
	if err != nil {
		log.Fatal(err)
	}
	idVideo := c.Query("idvideo")

	log.Println(idVideo)
	log.Println(file.Filename)
	path := "temp/" + file.Filename

	err = RemoveContents("./temp")
	if err != nil {
		log.Fatal(err)
	}

	// TODO delete this test function
	err = c.SaveUploadedFile(file, path)
	if err != nil {
		log.Fatal(err)
	}

	c.JSON(http.StatusOK, file.Filename+"/"+idVideo)

	quality := services.DecomposeQuality(path)

	log.Println("Quality: ", quality)

	go handleThumbnail(path, idVideo)

	switch quality {
	case 1080:
		go handle240(path, idVideo)
		go handle360(path, idVideo)
		go handle480(path, idVideo)
		go handle720(path, idVideo)
		go handle1080(path, idVideo)
		break
	case 720:
		go handle240(path, idVideo)
		go handle360(path, idVideo)
		go handle480(path, idVideo)
		go handle720(path, idVideo)
		break
	case 480:
		go handle240(path, idVideo)
		go handle360(path, idVideo)
		go handle480(path, idVideo)
		break
	case 360:
		go handle240(path, idVideo)
		go handle360(path, idVideo)
		break
	case 240:
		go handle240(path, idVideo)
		break
	}
}

func RemoveContents(dir string) error {
	files, err := filepath.Glob(filepath.Join(dir, "*"))
	if err != nil {
		return err
	}
	for _, file := range files {
		err = os.RemoveAll(file)
		if err != nil {
			return err
		}
	}
	return nil
}

func SendVideoFormat(format int, path string, idVideo string) {
	extraParams := map[string]string{
		"format": strconv.Itoa(format),
	}
	request, err := NewfileUploadRequest("http://api:8080/format/"+idVideo, extraParams, "source", path)
	if err != nil {
		log.Fatal(err)
	}
	client := &http.Client{}
	resp, err := client.Do(request)
	if err != nil {
		log.Fatal(err)
	} else {
		fmt.Println(resp.StatusCode)
	}
	e := os.Remove(path)
	if e != nil {
		log.Fatal(e)
	}
}

func SendThumbnail(path string, idVideo string) {
	request, err := NewfileUploadThumbnail("http://api:8080/video/"+idVideo, "source", path)
	if err != nil {
		log.Fatal(err)
	}
	client := &http.Client{}
	resp, err := client.Do(request)
	if err != nil {
		log.Fatal(err)
	} else {
		fmt.Println(resp.StatusCode)
	}
	e := os.Remove(path)
	if e != nil {
		log.Fatal(e)
	}
}

func NewfileUploadRequest(uri string, params map[string]string, paramName, path string) (*http.Request, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	fileContents, err := ioutil.ReadAll(file)
	if err != nil {
		return nil, err
	}
	fi, err := file.Stat()
	if err != nil {
		return nil, err
	}
	file.Close()

	body := new(bytes.Buffer)
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile(paramName, fi.Name())
	if err != nil {
		return nil, err
	}
	part.Write(fileContents)

	for key, val := range params {
		_ = writer.WriteField(key, val)
	}
	err = writer.Close()
	if err != nil {
		return nil, err
	}

	request, _ := http.NewRequest("POST", uri, body)
	request.Header.Add("Content-Type", writer.FormDataContentType())

	return request, nil
}

func NewfileUploadThumbnail(uri string, paramName, path string) (*http.Request, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	fileContents, err := ioutil.ReadAll(file)
	if err != nil {
		return nil, err
	}
	fi, err := file.Stat()
	if err != nil {
		return nil, err
	}
	file.Close()

	body := new(bytes.Buffer)
	writer := multipart.NewWriter(body)
	part, err := writer.CreateFormFile(paramName, fi.Name())
	if err != nil {
		return nil, err
	}
	part.Write(fileContents)

	err = writer.Close()
	if err != nil {
		return nil, err
	}

	request, _ := http.NewRequest("PUT", uri, body)
	request.Header.Add("Content-Type", writer.FormDataContentType())

	return request, nil
}

func handleThumbnail(path string, idVideo string) {
	services.ConvertThumbnail(path)
	SendThumbnail(path + ".jpg", idVideo)
}

func handle1080(path string, idVideo string) {
	services.ConvertVideoTo1080(path)
	SendVideoFormat(1080, strings.Replace(path + ".mp4", "base", "1080", -1), idVideo)
}

func handle720(path string, idVideo string) {
	services.ConvertVideoTo720(path)
	SendVideoFormat(720, strings.Replace(path + ".mp4", "base", "720", -1), idVideo)
}

func handle480(path string, idVideo string) {
	services.ConvertVideoTo480(path)
	SendVideoFormat(480, strings.Replace(path + ".mp4", "base", "480", -1), idVideo)
}

func handle360(path string, idVideo string) {
	services.ConvertVideoTo360(path)
	SendVideoFormat(360, strings.Replace(path + ".mp4", "base", "360", -1), idVideo)
}

func handle240(path string, idVideo string) {
	services.ConvertVideoTo240(path)
	SendVideoFormat(240, strings.Replace(path + ".mp4", "base", "240", -1), idVideo)
}
