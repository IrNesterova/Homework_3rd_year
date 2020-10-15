    import React, { Component } from 'react'
    import AuthenticationService from '../service/AuthenticationService';
    import '../assets/login.css'
    class RegisterComponent extends Component {
    
        constructor(props) {
            super(props);
    
            this.state = {
                login: '',
                email: '',
                password: ''
            };
    
            this.handleChange = this.handleChange.bind(this);
            this.registerClicked = this.registerClicked.bind(this)
        }
    
        handleChange(event) {
            this.setState(
                {
                    [event.target.name]
                        : event.target.value
                }
            )
        }
    
        registerClicked() {
            AuthenticationService
                .executeJwtRegister(this.state.login, this.state.email, this.state.password)
                .then((response) => {
                    console.log(response.data);
                    //AuthenticationService.registerSuccessfulLoginForJwt(this.state.login, response.data.value)
                    this.props.history.push(`/login`)
                }).catch(() => {}
            )
    
        }
    
        render() {
            return (
                <div className={"auth-wrapper"}>
                    <div className={"auth-inner"}>
                <form>
                    <h3>Register</h3>
                    <div className={"form-group"}>
                         <input type="text" name="login" value={this.state.login} placeholder={"login"} onChange={this.handleChange} />
                </div>
                    <div className={"form-group"}>
                        <input type="text" name="email" placeholder={"email"} value={this.state.email} onChange={this.handleChange} />
                    </div>
                    <div className={"form-group"}>
                        <input type="password" name="password" placeholder={"password"} value={this.state.password} onChange={this.handleChange} />
                    </div>
                        <button className="btn btn-primary btn-bloc" onClick={this.registerClicked}>Register</button>

                </form>
                    </div>
                </div>
            )
        }
    }
    
    export default RegisterComponent