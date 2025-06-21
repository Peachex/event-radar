export class EventDataUtil {
  static getEventDataSearchableFields(): string[] {
    return [
      'title',
      'dateStr',
      'date.startDate',
      'date.endDate',
      'category',
      'priceStr',
      'price.minPrice',
      'price.maxPrice',
      'location.country',
      'location.city',
      'sourceType',
    ];
  }
}
