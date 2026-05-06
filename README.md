# Water Leakage Management System

供水管网漏损控制管理系统。

## 1. Project Overview

This project is a B/S architecture web management system for water supply network leakage control.

The first-stage goal is to build a basic enterprise management framework, including:

- User login
- Dynamic captcha
- Dashboard
- System configuration
- Menu management
- Role management
- User management
- Role-menu permission binding
- User-role binding
- Dynamic menu filtering based on user roles

## 2. Technology Stack

### Backend

- JDK 21
- Spring Boot 3
- MyBatis Plus
- PostgreSQL
- Maven

### Frontend

- Vue 3
- Vue Router
- Element Plus
- ECharts
- Vite

### Tools

- Git
- GitHub
- Cursor
- VS Code compatible development environment

## 3. Project Structure

```text
water-leakage-management-system
├── backend
├── frontend
├── docs
├── scripts
└── sql

4. Environment Requirements

* JDK 21+
* Maven 3.9+
* Node.js 18+
* PostgreSQL 14+
* Git

5. Development Plan

Stage 1: Basic System Framework

* Backend project initialization
* Frontend project initialization
* Database initialization
* Login and captcha
* Dashboard
* System configuration
* Menu management
* Role management
* User management
* Permission filtering

Stage 2: Business Modules

* Area management
* Server resource monitoring
* Scheduled task management
* Leakage data dashboard
* Alarm and event management