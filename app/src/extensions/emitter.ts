import mitt from 'mitt';
import { EntryUpdateEvent } from '~/engine/events';

type Events = {
    'entry-update': EntryUpdateEvent;
    'login-expired': void;
};

export const bus = mitt<Events>();
