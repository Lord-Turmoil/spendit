export class State {
    public setLoggedIn(state: boolean): void {
        localStorage.setItem('state', JSON.stringify(state));
    }

    public isLoggedIn(): boolean {
        const state = localStorage.getItem('state');
        if (state) {
            return state === 'true';
        }
        return false;
    }
}

export const state = new State();
