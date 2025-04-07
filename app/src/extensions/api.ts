import axios, { AxiosError, AxiosInstance } from 'axios';
import router from '~/extensions/router';

class Api {
    private readonly _api: AxiosInstance;

    constructor() {
        this._api = axios.create({
            withCredentials: true,
            baseURL: import.meta.env.VITE_SPENDIT_API || '/api'
        });
    }

    axios() {
        return this._api;
    }

    _getDto(err: AxiosError): ApiResponse {
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
        const defaultDto = {
            status: response.status ?? 66,
            message: '服务器异常，请稍后再试',
            data: null
        };

        // no data
        if (!response.data || response.data === '') {
            return defaultDto;
        }

        const data: object = err.response.data as object;
        if (Object.hasOwn(data, 'status') && Object.hasOwn(data, 'message')) {
            return data as ApiResponse;
        } else {
            return defaultDto;
        }
    }

    async post(url: string, body: object = {}) {
        return await this._api
            .post(url, body)
            .then((res) => {
                console.log(res);
                // 200 must be our custom data
                return res.data;
            })
            .catch((err) => {
                console.log(err);
                const dto = this._getDto(err);
                if (dto.status === 401) {
                    router.push('/login');
                }
                return dto;
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
                const dto = this._getDto(err);
                if (dto.status === 401) {
                    router.push('/login');
                }
                return dto;
            });
    }
}

export const api = new Api();

export interface ApiResponse {
    status: number;
    message: string;
    data?: any;
}
