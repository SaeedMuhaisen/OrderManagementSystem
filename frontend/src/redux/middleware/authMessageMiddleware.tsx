let authActions: any = null;

export const setAuthMessageMiddlewareActions = (actions: any) => {
    authActions = actions;
};

export const authMessageMiddleware = (store: any) => (next: any) => (action: any) => {
    if (authActions && action.type === 'user/forceSignout') {
        console.log('middle ware activated!');
        authActions.forceSignout();
    }
    return next(action);
};