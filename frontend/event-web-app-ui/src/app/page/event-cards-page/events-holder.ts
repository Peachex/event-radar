import { EventData } from '../../core/model/event-data';

export class EventsHolder {
  //todo: Temp class that mock events retrieving.

  private static events: EventData[] = [
    {
      id: '68473f260cc74a56610a1bda',
      title:
        'XXIII Международный детский музыкальный конкурс «Витебск». День 2',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '9 июля',
      date: {
        startDate: [2025, 7, 9],
        endDate: [2025, 7, 9],
      },
      category: 'События',
      sourceType: 'AFISHA_RELAX',
      eventLink:
        'https://afisha.relax.by/event/11092643-xx-mezhdunarodnyj-detskij-muzykalynyj-konkurs-vitebsk-deny-2/minsk/',
      imageLink: '',
    },
    {
      id: '68473f260cc74a56610a1bdb',
      title:
        'BETERA - чемпионат Республики Беларусь по футболу сезона 2025 года среди команд высшей лиги ФК БАТЭ - ФК Неман',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '14 июня - 15 июня',
      date: {
        startDate: [2025, 6, 14],
        endDate: [2025, 6, 15],
      },
      category: 'Спорт',
      sourceType: 'BYCARD',
      priceStr: 'от 7.00 - до 19.00 руб.',
      price: {
        minPrice: 7.0,
        maxPrice: 19.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/sport/4396597',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/81ABfvuWS.png',
    },
    {
      id: '68473f260cc74a56610a1bdc',
      title: 'Концерт Максима Фадеева',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '11 октября',
      date: {
        startDate: [2025, 10, 11],
        endDate: [2025, 10, 11],
      },
      category: 'Концерты',
      sourceType: 'AFISHA_RELAX',
      eventLink:
        'https://afisha.relax.by/conserts/10853803-koncert-maksima-fadejeva/minsk/',
      imageLink:
        'https://ms1.relax.by/images/20e977928c5e456fb1e69ef5e6546406/thumb/w%3D600%2Ch%3D900%2Cq%3D81/afisha_event_photo/53/65/fb/5365fbf4fe0c1d70ca7fee7764b44e4a.jpg',
    },
    {
      id: '68473f260cc74a56610a1bdd',
      title: 'Лило и Стич',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: 'по 19 июня',
      date: {
        startDate: null,
        endDate: [2025, 6, 19],
      },
      category: 'Рекомендуем',
      sourceType: 'BYCARD',
      priceStr: 'до 35.00 руб.',
      price: {
        minPrice: null,
        maxPrice: 35.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/top/4195776',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/6alD3U6Ve.jpg',
    },
    {
      id: '68473f260cc74a56610a1bde',
      title: 'Три богатыря. Ни дня без подвига 2',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '12 июня - 19 июня',
      date: {
        startDate: [2025, 6, 12],
        endDate: [2025, 6, 19],
      },
      category: 'Рекомендуем',
      sourceType: 'BYCARD',
      priceStr: 'до 31.00 руб.',
      price: {
        minPrice: null,
        maxPrice: 31.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/top/3955564',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/4ey6TAtv6.jpg',
    },
    {
      id: '68473f260cc74a56610a1bdf',
      title: 'Жизнь Чака',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: 'до 18 июня',
      date: {
        startDate: null,
        endDate: [2025, 6, 18],
      },
      category: 'Кино',
      sourceType: 'AFISHA_RELAX',
      eventLink: 'https://afisha.relax.by/kino/11136439-zhizny-chaka/minsk/',
      imageLink:
        'https://ms1.relax.by/images/20e977928c5e456fb1e69ef5e6546406/thumb/w%3D600%2Ch%3D900%2Cq%3D81/afisha_event_photo/ff/b0/31/ffb0315fead5e7a9c25530f1b0c897a6.jpg',
    },
    {
      id: '68473f260cc74a56610a1be0',
      title: 'Детская железная дорога 2025',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '11 июня - 13 сентября',
      date: {
        startDate: [2025, 6, 11],
        endDate: [2025, 9, 13],
      },
      category: 'Рекомендуем',
      sourceType: 'BYCARD',
      priceStr: '10.00 руб.',
      price: {
        minPrice: 10.0,
        maxPrice: 10.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/top/3953272',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/174601830382.png',
    },
    {
      id: '68473f260cc74a56610a1be1',
      title: 'Жизнь Чака',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: 'по 19 июня',
      date: {
        startDate: null,
        endDate: [2025, 6, 19],
      },
      category: 'Кино',
      sourceType: 'BYCARD',
      priceStr: 'от 6.00 - до 32.00 руб.',
      price: {
        minPrice: 6.0,
        maxPrice: 32.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/kino/4263333',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/3DTxbssE9.jpg',
    },
    {
      id: '68473f260cc74a56610a1be2',
      title: 'Дискотека СССР',
      location: {
        id: '681a56737aa4314c66abc32f',
        country: 'Belarus',
        city: 'Minsk',
      },
      dateStr: '9 августа',
      date: {
        startDate: [2025, 8, 9],
        endDate: [2025, 8, 9],
      },
      category: 'Рекомендуем',
      sourceType: 'BYCARD',
      priceStr: 'от 58.50 - до 200.00 руб.',
      price: {
        minPrice: 58.5,
        maxPrice: 200.0,
      },
      eventLink: 'https://bycard.by/afisha/minsk/top/340243',
      imageLink:
        'https://abws.bycard.by/uploads/events/thumbs/170x240/2HF0YvjEV.jpg',
    },
  ];

  static getEvents(): EventData[] {
    return this.events;
  }
}
