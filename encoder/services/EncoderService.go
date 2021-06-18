package services

import (
	"bytes"
	"log"
	"os/exec"
	"strconv"
	"strings"
)

func DecomposeBitrate(path string) int {

	cmd, err := exec.Command("ffprobe",
		"-v", "error", "-select_streams", "v:0", "-show_entries",
		"stream=bit_rate", "-of",
		"default=noprint_wrappers=1", path,
	).Output()
	if err != nil {
		log.Fatal(err)
	}

	temp, errA := strconv.Atoi(strings.TrimSpace(strings.Replace(string(cmd), "bit_rate=", "", -1)))
	if errA != nil {
		log.Fatal(err)
	}

	return temp
}

func DecomposeWidth(path string) int {

	cmd, err := exec.Command("ffprobe",
		"-v", "error", "-select_streams", "v:0", "-show_entries",
		"stream=width", "-of",
		"default=noprint_wrappers=1", path,
	).Output()
	if err != nil {
		log.Fatal(err)
	}

	temp, errA := strconv.Atoi(strings.TrimSpace(strings.Replace(string(cmd), "width=", "", -1)))
	if errA != nil {
		log.Fatal(err)
	}

	return temp
}

func DecomposeHeight(path string) int {

	cmd, err := exec.Command("ffprobe",
		"-v", "error", "-select_streams", "v:0", "-show_entries",
		"stream=height", "-of",
		"default=noprint_wrappers=1", path,
	).Output()
	if err != nil {
		log.Fatal(err)
	}

	temp, errA := strconv.Atoi(strings.TrimSpace(strings.Replace(string(cmd), "height=", "", -1)))
	if errA != nil {
		log.Fatal(err)
	}

	return temp
}

func ConvertVideoTo720(path string){
	var err error

	log.Println("Encoder: Encoding video to 720p")
	cmd := exec.Command("ffmpeg",
		"-i", path, "-preset", "slow", "-b:v",
		"1280k", "-vf", "scale=1280:720", strings.Replace(path + ".mp4", "base", "720", -1),
	)

	stdoutBuf := &bytes.Buffer{}
	stderrBuf := &bytes.Buffer{}
	cmd.Stdout = stdoutBuf
	cmd.Stderr = stderrBuf

	if err = cmd.Run(); err != nil {
		log.Println("cmd failed: %s", strings.Join(cmd.Args, " "))
		log.Println(string(stderrBuf.Bytes()))
		log.Fatal(err)
	}

	log.Println("Encoder: 720p - video encoded")
}

func ConvertVideoTo1080(path string){
	var err error

	log.Println("Encoder: Encoding video to 1080p")
	cmd := exec.Command("ffmpeg",
		"-i", path, "-preset", "slow", "-b:v",
		"1920k", "-vf", "scale=1920:1080", strings.Replace(path + ".mp4", "base", "1080", -1),
	)

	stdoutBuf := &bytes.Buffer{}
	stderrBuf := &bytes.Buffer{}
	cmd.Stdout = stdoutBuf
	cmd.Stderr = stderrBuf

	if err = cmd.Run(); err != nil {
		log.Println("cmd failed: %s", strings.Join(cmd.Args, " "))
		log.Println(string(stderrBuf.Bytes()))
		log.Fatal(err)
	}

	log.Println("Encoder: 1080p - video encoded")
}

func ConvertVideoTo480(path string){
	var err error

	log.Println("Encoder: Encoding video to 480p")
	cmd := exec.Command("ffmpeg",
		"-i", path, "-preset", "slow", "-b:v",
		"854k", "-vf", "scale=854:480", strings.Replace(path + ".mp4", "base", "480", -1),
	)

	stdoutBuf := &bytes.Buffer{}
	stderrBuf := &bytes.Buffer{}
	cmd.Stdout = stdoutBuf
	cmd.Stderr = stderrBuf

	if err = cmd.Run(); err != nil {
		log.Println("cmd failed: %s", strings.Join(cmd.Args, " "))
		log.Println(string(stderrBuf.Bytes()))
		log.Fatal(err)
	}

	log.Println("Encoder: 480p - video encoded")
}

func ConvertVideoTo360(path string){
	var err error

	log.Println("Encoder: Encoding video to 360p")
	cmd := exec.Command("ffmpeg",
		"-i", path, "-preset", "slow", "-b:v",
		"640k", "-vf", "scale=640:360", strings.Replace(path + ".mp4", "base", "360", -1),
	)

	stdoutBuf := &bytes.Buffer{}
	stderrBuf := &bytes.Buffer{}
	cmd.Stdout = stdoutBuf
	cmd.Stderr = stderrBuf

	if err = cmd.Run(); err != nil {
		log.Println("cmd failed: %s", strings.Join(cmd.Args, " "))
		log.Println(string(stderrBuf.Bytes()))
		log.Fatal(err)
	}

	log.Println("Encoder: 360p - video encoded")
}

func ConvertVideoTo240(path string){
	var err error

	log.Println("Encoder: Encoding video to 240p")
	cmd := exec.Command("ffmpeg",
		"-i", path, "-preset", "slow", "-b:v",
		"426k", "-vf", "scale=426:240", strings.Replace(path + ".mp4", "base", "240", -1),
	)

	stdoutBuf := &bytes.Buffer{}
	stderrBuf := &bytes.Buffer{}
	cmd.Stdout = stdoutBuf
	cmd.Stderr = stderrBuf

	if err = cmd.Run(); err != nil {
		log.Println("cmd failed: %s", strings.Join(cmd.Args, " "))
		log.Println(string(stderrBuf.Bytes()))
		log.Fatal(err)
	}

	log.Println("Encoder: 240p - video encoded")
}

func DecomposeQuality(path string) int {
	bitrate := DecomposeBitrate(path)
	width := DecomposeWidth(path)
	height := DecomposeHeight(path)

	log.Println("Bitrate: ", bitrate)
	log.Println("Resolution: ", width, "x", height)

	if (bitrate > 1280000 && width >= 1920 && height >= 1080) {
		return 1080
	} else if (bitrate > 854000 && width >= 1280 && height >= 720) {
		return 720
	} else if (bitrate > 640000 && width >= 854 && height >= 480) {
		return 480
	} else if (bitrate > 426000 && width >= 640 && height >= 360) {
		return 360
	} else {
		return 240
	}
}
