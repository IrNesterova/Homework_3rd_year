import {Component} from "react";
import * as React from "react";
import axios from 'axios'

export default class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            message: '',
            ChatMessages: []
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    componentDidMount() {
        if (window.localStorage.getItem("AUTH") != null) {
            axios({
                url: 'http://localhost:8080/getAll',
                method: "get"
            }).then((response) => {
                // eslint-disable-next-line no-console
                for (var i = 0; i < response.data.length; i++) {
                    this.state.ChatMessages.unshift((response.data)[i]);
                }
            })
            //     axios({
            //         url: 'http://localhost:8080/messages',
            //         method: "post",
            //         headers: {
            //             "AUTH": window.localStorage.getItem("AUTH")
            //         },
            //         data: {
            //             'text': this.state.message
            //         }
            //         // eslint-disable-next-line no-unused-vars
            //     }).then((response) => {
            //         this.receiveMessage();
            //     });
            // } else {
            //     window.location.href = '/login';
            // }

        }
    }

    receiveMessage() {
        if (window.localStorage.getItem("AUTH") != null) {
            axios({
                url: "http://localhost:8080/messages",
                method: "get",
                headers: {
                    "Access-Control-Allow-Origin": "*",
                    "AUTH": window.localStorage.getItem("AUTH")
                }
            }).then((response) => {
                console.log(response);
                if (response.status === 200) {
                    this.state.ChatMessages.unshift(response.data[0]);
                    this.receiveMessage();
                }
            });
        } else {
            window.location.href = '/login';
        }
    }

    sendMessage() {
        if (document.getElementById("message").value !== "") {
            if (window.localStorage.getItem("AUTH") != null) {
                axios({
                    url: "http://localhost:8080/messages",
                    method: "post",
                    data: {
                        'text': document.getElementById("message").value
                    },
                    headers: {
                        "Access-Control-Allow-Origin": "*",
                        "AUTH": window.localStorage.getItem("AUTH")
                    }
                }).then((response) => {
                    if (response.status === 200) {
                        // eslint-disable-next-line no-console
                        this.receiveMessage();
                    }
                });
            } else {
                window.location.href = '/login'
            }
        }
    }

    handleSubmit(event) {
        event.preventDefault();
        if (this.state.message !== " ") {
            this.sendMessage();
        }
    }

    handleChange(event) {
        this.setState({message: event.target.value});
    }


    render() {
        return (
            <div className={"chatContainer"}>
                <ul>
                    {
                        this.state.ChatMessages.map((messageDto, index)=>{
                        return (
                            <li className={"messages"} key={index}>
                                <h6>{messageDto.author}</h6><h5>{messageDto.text}</h5>
                            </li>
                        )
                        }
                    )}
                </ul>
            <form onSubmit={this.handleSubmit}>
                <label >
                    <input type={"text"} id={"message"} value={this.state.message} onChange={this.handleChange}/>
                    <button type="submit" className="btn btn-primary btn-block">Submit</button>
                </label>
            </form>
            </div>
        )
    }
}