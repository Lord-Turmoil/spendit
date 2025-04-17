import axios, { AxiosError, AxiosInstance } from 'axios';

import router from '~/extensions/router';
import alertify from '~/extensions/alertify';

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
                message: 'Network Error, try again later',
                data: {
                    name: error.name,
                    message: error.message
                }
            };
        }
        const response = error.response;
        const defaultDto = {
            status: response.status ?? 66,
            message: 'Server Error, try again later',
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
                this.handlePermissionError(dto, autoRedirect);
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
                this.handlePermissionError(dto, autoRedirect);
                return dto;
            });
    }

    private handlePermissionError(dto: ApiResponse, autoRedirect: boolean) {
        if (dto.status === 401) {
            alertify.error('Login required');
            if (autoRedirect) {
                router.push('/login');
            }
        } else if (dto.status === 403) {
            alertify.error('Insolent boy');
            if (autoRedirect) {
                router.push('/login');
            }
        }
    }
}

export const api = new Api();

export interface ApiResponse {
    status: number;
    message: string;
    data?: any;
}
