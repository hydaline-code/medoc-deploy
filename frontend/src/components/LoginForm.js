import React, { useState } from 'react';

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const [error, setError] = useState(null);
	

    // We won't handle form submission just yet, this will be for the UI only
    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <div className="w-full max-w-md bg-white p-8 rounded-lg shadow-lg">
                <div className="text-center mb-6">
                   <img src="/logo.jpg" alt="Logo" className="mx-auto" width="100px" />
                </div>
                {error && <p className="text-red-500 text-center mb-4">{error}</p>}
                <form className="space-y-4">
                    <div>
                        <input
                            type="email"
                            name="email"
                            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            placeholder="E-mail"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>
                    <div>
                        <input
                            type="password"
                            name="password"
                            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500"
                            placeholder="Password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div className="flex items-center justify-between">
                        <div className="flex items-center">
                            <input
                                type="checkbox"
                                name="remember-me"
                                className="mr-2"
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            <label className="text-sm text-gray-700">Remember Me</label>
                        </div>
                        <a href="/forgot_password" className="text-sm text-indigo-500">Forgot Password?</a>
                    </div>
                    <div>
                        <button type="submit" className="w-full bg-indigo-600 text-white p-3 rounded-lg hover:bg-indigo-700">
                            Login
                        </button>
                    </div>
                </form>
                <div className="mt-4 text-center">
                    <p>Don't have an account? <a href="/register" className="text-indigo-500">Sign up</a></p>
                </div>
            </div>
        </div>
    );
};

export default LoginForm;
