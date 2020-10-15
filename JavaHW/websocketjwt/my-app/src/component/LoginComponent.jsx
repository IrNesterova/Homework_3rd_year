import React, { Component } from 'react'
import AuthenticationService from '../service/AuthenticationService';
import '../assets/login.css'

class LoginComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            login: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMessage: false
        }

        this.handleChange = this.handleChange.bind(this)
        this.loginClicked = this.loginClicked.bind(this)
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }

    loginClicked() {
        AuthenticationService
            .executeJwtLogin(this.state.login, this.state.password)
            .then((response) => {
                console.log(response.data);
                AuthenticationService.registerSuccessfulLoginForJwt(this.state.login, response.data.tokenValue)
                this.props.history.push(`/chat`)
            }).catch(() => {
            this.setState({ showSuccessMessage: false })
            this.setState({ hasLoginFailed: true })
        })

    }

    render() {
        return (
            <div className={"auth-wrapper"}>
                <div className={"auth-inner"}>
                    <h3>Sign in</h3>
                <form>
                    {/*<ShowInvalidCredentials hasLoginFailed={this.state.hasLoginFailed}/>*/}
                    {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
                    {this.state.showSuccessMessage && <div>Login Sucessful</div>}
                    {/*<ShowLoginSuccessMessage showSuccessMessage={this.state.showSuccessMessage}/>*/}
                    <div className={"form-group"}>
                        <input type="text" name="login" className={"form-control"} placeholder={"Login"} value={this.state.login} onChange={this.handleChange} /></div>
                    <div className={"form-group"}>
                    <input type="password" name="password" placeholder={"Username"} className={"form-control"} value={this.state.password} onChange={this.handleChange} />
                    </div>
                    <button type={"submit"} className="btn btn-primary btn-block" onClick={this.loginClicked}>Login</button>
                </form>
            </div>
            </div>
        )
    }
}

export default LoginComponent