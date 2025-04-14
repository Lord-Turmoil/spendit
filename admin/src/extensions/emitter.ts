import mitt from 'mitt';
import { Invitation } from '~/extensions/models';

type Events = {
    'create-invitation': Invitation;
    'cancel-invitation': Invitation;
};

export const bus = mitt<Events>();
