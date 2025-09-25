# 📚 webtoon-service

A Java-based **Webtoon Scraping & Upload Service** that allows dynamic extraction of webtoon chapter images from multiple providers and uploads them to AWS S3.

Built with extensibility and scalability in mind using the **Strategy Pattern**, this service supports multiple webtoon providers and handles scraping, zipping, and uploading operations in a clean, modular architecture.

---

## 🚀 Features

- ✅ Scrape chapter images from supported webtoon providers
- ✅ Dynamically generate chapter URLs using suffix and patterns
- ✅ Extract, sort, and zip images per chapter
- ✅ Upload zip files to AWS S3
- ✅ Designed for **multi-provider support**
- ✅ Headless Chrome scraping via Selenium WebDriver
- ✅ Auto-cleanup of temp files after upload

---

## 📦 Supported Providers

| Provider ID | Base URL                  | Notes                       |
|-------------|---------------------------|-----------------------------|
| `asura`     | https://asuracomic.net    | Uses `{title}-{suffix}`    |
| `comick`    | https://comick.art        | Uses `{title}/{suffix}`    |
| _(more coming)_ | _easily pluggable_    | Add via strategy pattern    |

---

## ⚙️ Configuration

This service does **not** require external `.properties` files to run scraping.  
For AWS S3 upload, make sure to configure the `S3StorageClient` properly using:

- `AWS_ACCESS_KEY`
- `AWS_SECRET_KEY`
- `AWS_REGION`
- `AWS_S3_BUCKET`

You can inject these via:
- Environment variables
- `application.yml`
- Spring `@ConfigurationProperties`

---

## 🧠 Design Patterns Used

- **Strategy Pattern** — to abstract per-provider scraping logic
- **Factory Pattern** — to return scraping strategy based on provider ID
- **Constants Class** — for regex, xpath, and other shared constants

---

## 🧪 Sample API Usage

```json
POST /upload-webtoon-chapter

{
  "title": "nano-machine",
  "chapter": "244",
  "provider": {
    "id": "asura",
    "suffix": "0869fe98"
  }
} 
