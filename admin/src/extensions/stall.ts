export function delay(ms: number): Promise<void> {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve();
        }, ms);
    });
}

export const EXTRA_STALL = 2000;
export const LONG_STALL = 1000;
export const NORMAL_STALL = 500;
export const SHORT_STALL = 300;
export const NO_STALL = 0;
export const ACTIVE_STALL = NORMAL_STALL;

export async function stall(task: any, ms = ACTIVE_STALL): Promise<any> {
    if (ms == 0) {
        return await task;
    }
    const [ret, _] = await Promise.all([task, delay(ms)]);
    return ret;
}
