import React, {Component} from 'react';
import axios from 'axios'
import '../login.css'
export default class LoginPage extends Component {
    render() {
        return (
            <div className={"auth-wrapper"}>
                <div className={"auth-inner"}>
                <form onSubmit={login}>
                <h3>Sign In</h3>

                <div className="form-group">
                    <label>Username</label>
                    <input type="username" id={"login"} className="form-control" placeholder="Enter email" />
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input type="password" id={"password"} className="form-control" placeholder="Enter password" />
                </div>

                <div className="form-group">
                    <div className="custom-control custom-checkbox">
                        <input type="checkbox" className="custom-control-input" id="customCheck1" />
                        <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                    </div>
                </div>

                <button type="submit" className="btn btn-primary btn-block">Submit</button>
                </form>
                </div>
            </div>
            // <div className="container">
            //     <div className="main">
            //         <div className="maincentre">
            //             <form onSubmit={login} className="form-signin"
            //                   style={{paddingTop: '50px', minWidth: '300px', maxWidth: '300px'}}>
            //                 <h2 className="shadowText" style={{marginBottom: '2px'}}>Please sign in</h2>
            //                 <input id="login" name="login" type="text" className="form-control" placeholder="Login"
            //                        required autoFocus style={{marginBottom: '3px'}}/>
            //                 <input id="password" name="password" type="password" className="form-control"
            //                        placeholder="Password" required/>
            //                 <br/>
            //                 <button className="btn btn-lg btn-dark btn-block" type="submit">Login</button>
            //                 <a href="/registration">to registration page</a>
            //             </form>
            //             <br/>
            //         </div>
            //     </div>
            // </div>
        )
    }
}
function login(e) {
    e.preventDefault();
    axios({
        method: 'post',
        contentType: "application/json",
        url: 'http://localhost:8080/login',
        data: {
            login: document.getElementById('login').value,
            password: document.getElementById('password').value
        }
    }).then((response =>{
            if (response.status == 200){
                // eslint-disable-next-line no-console
                console.log(response);
                window.localStorage.setItem("AUTH", response.data.value);
                window.location.href = '/chat'
            }
    })
    )
}

