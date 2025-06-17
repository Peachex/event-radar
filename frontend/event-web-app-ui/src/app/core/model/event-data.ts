export interface EventData {
  id: string;
  title: string;
  dateStr: string;
  date: {
    startDate: number[] | null;
    endDate: number[] | null;
  };
  category: string;
  priceStr?: string;
  price?: {
    minPrice: number | null;
    maxPrice: number | null;
  };
  imageLink?: string;
  eventLink: string;
  location: {
    id: string;
    country: string;
    city: string;
  };
  sourceType: string;
}
