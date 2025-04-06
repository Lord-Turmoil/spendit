import axios, { AxiosError, AxiosInstance } from 'axios';

class Api {
    private readonly _api: AxiosInstance;

    constructor() {
        this._api = axios.create({
            withCredentials: true,
            baseURL: '/api'
        });
    }

    axios() {
        return this._api;
    }

    _getDto(err: AxiosError) {
        if (!Object.hasOwn(err, 'response')) {
            return {
                status: 101,
                message: '网络异常，请稍后再试',
                data: {
                    name: err.name,
                    message: err.message
                }
            };
        }
        const response = err.response;
        const ret = { status: response.status ?? 66, message: err.message, data: null };

        // no data
        if (!response.data || response.data === '') {
            return ret;
        }

        const data: object = err.response.data as object;
        if (Object.hasOwn(data, 'status')) {
            return data;
        } else {
            return {
                status: data['status'],
                message: data['title'],
                data: null
            };
        }
    }

    async post(url: string, body: object = {}) {
        return await this._api
            .post(url, body)
            .then((res) => {
                // 200 must be our custom data
                return res.data;
            })
            .catch((err) => {
                return this._getDto(err);
            });
    }

    async get(url: string, params: object = {}) {
        return await this._api
            .get(url, { params: params })
            .then((res) => {
                // 200 must be our custom data
                return res.data;
            })
            .catch((err) => {
                return this._getDto(err);
            });
    }
}

export const api = new Api();

export interface ApiResponse {
    status: number;
    message: string;
    data?: any;
}
