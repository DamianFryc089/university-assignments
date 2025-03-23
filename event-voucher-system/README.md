# Event Voucher System

## Problem Overview

The goal of this system is to manage the process of handling vouchers for various attractions, offering CRUD (Create, Read, Update, Delete) operations on the processed data. The system will be implemented with separate applications for three roles:

- **KlientApp** (for the Client): Allows clients to browse offers, place orders, view the status of their orders, and submit declarations regarding their order fulfillment.
- **SprzedawcaApp** (for the Seller): Enables the seller to edit offers, approve orders, and update the status of orders.
- **OrganizatorApp** (for the Organizer): Allows the organizer to manage orders that are ready for fulfillment, update information about the fulfillment of an order.

Each actor interacts with the system through a separate application, and all data is stored persistently. The data is saved in a lightweight database - SQLite.

### Key Features:

1. **CRUD Operations**:
   - **Create**: Users can create new orders, offers, and records.
   - **Read**: Users can view orders, offers, and client details.
   - **Update**: Users can modify the status of orders, offers, and fulfillment information.
   - **Delete**: Users can remove orders, offers, and user records if necessary.

2. **Multiple User Roles**:
   - **Client**: Views available offers, places orders, and tracks order fulfillment.
   - **Seller**: Manages offers, approves orders, and updates order status.
   - **Organizer**: Manages the fulfillment of orders, updates the order status, and confirms participants.

3. **Voucher Management**:
   - The system supports the creation and management of vouchers for "last-minute" attractions, like hot air balloon rides.
   - In such cases, the **Organizator** will notify the clients about the planned event, and clients can declare their interest in participating. The organizer then confirms the participants before the event is scheduled.

4. **Persistence**:
   - Data is stored persistently in a local database SQLite, ensuring synchronization between the running instances of the applications.
   - This ensures that all the applications (KlientApp, SprzedawcaApp, OrganizatorApp) share the same data, preventing conflicts or loss of information.
