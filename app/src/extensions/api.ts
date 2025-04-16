import axios, { AxiosError, AxiosInstance } from 'axios';

import router from '~/extensions/router';

// disable cache
axios.defaults.headers['Cache-Control'] = 'no-cache';
axios.defaults.headers['Pragma'] = 'no-cache';
axios.defaults.headers['Expires'] = '0';

class Api {
    private readonly _api: AxiosInstance;

    constructor() {
        this._api = axios.create({
            withCredentials: true,
            baseURL: import.meta.env.VITE_SPENDIT_API || '/api'
        });
    }

    _getDto(error: AxiosError): ApiResponse {
        if (!Object.hasOwn(error, 'response')) {
            return {
                status: 101,
                message: '网络异常，请稍后再试',
                data: {
                    name: error.name,
                    message: error.message
                }
            };
        }
        const response = error.response;
        const defaultDto = {
            status: response.status ?? 66,
            message: '服务器异常，请稍后再试',
            data: null
        };

        // no data
        if (!response.data || response.data === '') {
            return defaultDto;
        }

        const data: object = error.response.data as object;
        if (Object.hasOwn(data, 'status') && Object.hasOwn(data, 'message')) {
            return data as ApiResponse;
        } else {
            return defaultDto;
        }
    }

    async post(
        url: string,
        body: object = {},
        autoRedirect: boolean = true
    ): Promise<ApiResponse> {
        return await this._api
            .post(url, body)
            .then((res) => {
                // 200 must be our custom data
                return res.data;
            })
            .catch((error) => {
                console.log(error);
                const dto = this._getDto(error);
                if (dto.status === 401 && autoRedirect) {
                    router.push('/login');
                }
                return dto;
            });
    }

    async get(
        url: string,
        params: object = {},
        autoRedirect: boolean = true
    ): Promise<ApiResponse> {
        return await this._api
            .get(url, { params: params })
            .then((res) => {
                // 200 must be our custom data
                return res.data;
            })
            .catch((error) => {
                const dto = this._getDto(error);
                if (dto.status === 401 && autoRedirect) {
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
