class InMemoryKfxImages {
  final String front;
  final String back;
  final String micr;

  const InMemoryKfxImages({
    this.front,
    this.back,
    this.micr,
  });

  factory InMemoryKfxImages.fromJson(Map<String, dynamic> json) =>
      InMemoryKfxImages(
        front: json['front'] as String,
        back: json['back'] as String,
        micr: json['micr'] as String,
      );
}
