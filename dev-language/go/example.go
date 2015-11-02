package main

import "fmt"
import (
	"time"
	"strings"
)

func init() { // initialization of package
	fmt.Println("init")
}

func Sum(a, b int) int {
	return a + b
}

func vals() (int, int) {
	return 3, 7
}

func plusPlus(a, b, c int) int {
	return a + b + c
}

func isGreater(x, y int) bool {
	if x > y {
		return true
	}
	return false
}

func sum(nums ...int) {
	fmt.Print(nums, " ")
	total := 0
	for _, num := range nums {
		total += num
	}
	fmt.Println(total)
}

// closure
func intSeq() func() int {
	i := 0

	return func() int {
		i += 1
		return i
	}
}

// pointer
func zeroptr(iptr *int) {
	*iptr = 0
}

//
func testRoutine(from string) {
	for i := 0; i < 3; i++ {
		fmt.Println(from, ":", i)
	}
}

func Map(vs []string, f func(string) string) []string {
	vsm := make([]string, len(vs))
	for i, v := range vs {
		vsm[i] = f(v)
	}
	return vsm
}

func main() {
	fmt.Println("hello, world")
	fmt.Println(isGreater(2, 1))

	var a string = "initial"
	fmt.Println(a)

	f := "short"
	fmt.Println(f)

	for j := 7; j <= 9; j++ {
		fmt.Println(j)
	}

	if num := 9; num < 0 {
		fmt.Println(num, "is negative")
	} else if num < 10 {
		fmt.Println(num, "has 1 digit")
	} else {
		fmt.Println(num, "has multiple digits")
	}

	// array
	b := [5]int{1, 2, 3, 4, 5}
	fmt.Println("dcl:", b)

	// map
	m := make(map[string]int)
	m["k1"] = 7
	m["k2"] = 13
	fmt.Println("map:", m)

	// range
	nums := []int{2, 3, 4}
	for i, num := range nums {
		if num == 3 {
			fmt.Println("index:", i)
		}
	}

	kvs := map[string]string{"a": "apple", "b": "banana"}
	for k, v := range kvs {
		fmt.Printf("%s -> %s\n", k, v)
	}

	for k1, v1 := range kvs {
		fmt.Printf("%s -> %s\n", k1, v1)
	}

	// pointer
	i := 1
	zeroptr(&i)

	// struct
	type person struct {
		name string
		age  int
	}
	fmt.Println(person{"cxf",30})

	fmt.Println(person{"Bob", 20})
	fmt.Println(person{name: "Alice", age: 30})

	//
	go testRoutine("goroutine")
	go func(msg string) {
		fmt.Println(msg)
	}("going")

	go func() {
		fmt.Println("hello, go")
	}()

	// channel
	messages := make(chan string)
	//Send a value into a channel using the channel <- syntax. Here we send "ping" to the messages channel we made above, from a new goroutine.
	go func() { messages <- "ping" }()
	// The <-channel syntax receives a value from the channel. Here we’ll receive the "ping" message we sent above and print it out.
	msg := <-messages
	fmt.Println(msg)

	// range over channel
	queue := make(chan string, 2)
	queue <- "one"
	queue <- "two"
	close(queue)
	for elem := range queue {
		fmt.Println(elem)
	}

	// tickers
	ticker := time.NewTicker(time.Millisecond * 500)
	go func() {
		for t := range ticker.C {
			fmt.Println("Tick at", t)
		}
	}()
	//Tickers can be stopped like timers. Once a ticker is stopped it won’t receive any more values on its channel. We’ll stop ours after 1500ms.
	time.Sleep(time.Millisecond * 1500)
	ticker.Stop()
	fmt.Println("Ticker stopped")

	//var input string
	//fmt.Scanln(&input)

	// collections functions
	fmt.Println(Map([]string{"CXF", "GJ"}, strings.ToLower))

	// string function
	fmt.Println(strings.Split("hell|go", "|"))
	fmt.Println("Join:      ", strings.Join([]string{"a", "b"}, "-"))

	// format
	fmt.Printf("|%6d|%6d|\n", 12, 345)


	// worker pool
	jobs := make(chan int, 100)
	results := make(chan int, 100)
	//This starts up 3 workers, initially blocked because there are no jobs yet.
	for w := 1; w <= 3; w++ {
		go worker(w, jobs, results)
	}
	//Here we send 9 jobs and then close that channel to indicate that’s all the work we have.
	for j := 1; j <= 9; j++ {
		jobs <- j
	}
	close(jobs)
	//Finally we collect all the results of the work.
	for a := 1; a <= 9; a++ {
		result := <-results
		fmt.Println(result)
	}

	var aInt Integer = 1
	fmt.Println(aInt.Less(2))
	aInt.Add(3)
	fmt.Println(aInt)

	var file1 IFile = new(File)
	var file4 ICloser = new(File)
	file1.Close()
	file4.Close()

}

type Integer int
func (a Integer) Less(b Integer) bool {
	return a < b
}

func (a *Integer) Add(b Integer) {
	*a += b
}

type Rect struct {
	x, y float64
	width, height float64
}

func (a *Rect) Area() float64 {
	return  a.height * a.width
}

// constrctor
func NewRect(x, y, width, height float64) *Rect {
	return &Rect{x, y, width, height}
}

//interface
type File struct {
	data int
}
func (f *File) Read(buf []byte) (n int, err error) {
	return 0, nil
}
func (f *File) Write(buf []byte) (n int, err error) {
	return 0, nil
}
func (f *File) Seek(off int64, whence int) (pos int64, err error) {
	return 0, nil
}
func (f *File) Close() error {
	return  nil

}

type IFile interface {
	Read(buf []byte) (n int, err error)
	Write(buf []byte) (n int, err error)
	Seek(off int64, whence int) (pos int64, err error)
	Close() error
}

type IReader interface {
	Read(buf []byte) (n int, err error)
}
type IWriter interface {
	Write(buf []byte) (n int, err error)
}
type ICloser interface {
	Close() error
}


func worker(id int, jobs <-chan int, results chan<- int) {
	for j := range jobs {
		fmt.Println("worker", id, "processing job", j)
		time.Sleep(time.Second)
		results <- j * 2
	}
}
