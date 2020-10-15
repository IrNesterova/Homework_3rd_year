import React from 'react';
import '../login.css'
export default function Registration(){
    return(
        // <div className="container">
        //     <div className="main">
        //         <div className="maincentre">
        //             <form onSubmit={registration} className="form-signin"
        //                   style={{paddingTop: '50px', minWidth: '300px', maxWidth: '300px'}}>
        //                 <h2 className="shadowText" style={{marginBottom: '2px'}}>Please sign in</h2>
        //                 <input id="login" name="login" type="text" className="form-control" placeholder="Login"
        //                        required autoFocus style={{marginBottom: '3px'}}/>
        //                 <input id="password" name="password" type="password" className="form-control"
        //                        placeholder="Password" required/>
        //                 <br/>
        //                 <button className="btn btn-lg btn-dark btn-block" type="submit">Signup</button>
        //                 <a href="/login">to registration page</a>
        //             </form>
        //             <br/>
        //         </div>
        //     </div>
        // </div>
        <div className={"auth-wrapper"}>
            <div className={"auth-inner"}>
        <form onSubmit={registration}>
            <h3>Sign Up</h3>

            <div className="form-group">
                <label>Username</label>
                <input type="login" id={"login"} className="form-control" placeholder="Enter email" />
            </div>

            <div className="form-group">
                <label>Password</label>
                <input type="password" id={"password"} className="form-control" placeholder="Enter password" />
            </div>

            <button type="submit" className="btn btn-primary btn-block">Sign Up</button>
            <p className="forgot-password text-right">
                Already registered <a href="#">sign in?</a>
            </p>
        </form>
            </div>
        </div>
    )
}
function registration(e){
    e.preventDefault();
    let url = 'http://localhost:8080/signUp';
    fetch(url, {
        method: 'post',
        mode: 'cors',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin':'*',
        },
        body:  JSON.stringify({ login: document.getElementById('login').value, password: document.getElementById('password').value})
    })
        .then(data => {
                window.location.replace(window.location.protocol + '/login');
            }
        ).catch(err => {
        // Do something for an error here
        console.log("Error Reading data " + err);
    });
}