# EduHub-sperimentale

EduHub è una piattaforma di gestione dell'istruzione che consente agli amministratori, insegnanti e studenti di gestire corsi, lezioni, quiz e materiali didattici.

## Installazione

1. Clona il repository:

```bash
git clone https://github.com/tuo-username/EduHub.git
cd EduHub/frontend
```

2. Installa le dipendenze:

```bash
npm install
```

3. Crea un file `.env` nella directory

frontend

e aggiungi le variabili d'ambiente necessarie:

```
VITE_API_URL=http://localhost:5000
VITE_STORAGE_URL=http://localhost:5000
```

## Avvio del Progetto

Per avviare il progetto in modalità di sviluppo, esegui:

```bash
npm run dev
```

## Struttura dei Componenti

### `src/components`

- **AddEntityModal.tsx**: Modale per aggiungere entità (studenti, docenti, ecc.).
- **AuthLayout.tsx**: Layout per le pagine di autenticazione.
- **ConfirmModal.tsx**: Modale di conferma.
- **EntityList.tsx**: Componente per visualizzare una lista di entità.
- **InfoCard.tsx**: Componente per visualizzare informazioni in un formato di card.
- **Layout.tsx**: Layout principale dell'applicazione.
- **Modal.tsx**: Componente generico per le modali.
- **Navbar.tsx**: Barra di navigazione.
- **Notification.tsx**: Componente per le notifiche.
- **ProtectedRoute.tsx**: Componente per proteggere le route.

### `src/pages`

- **Attendance**: Pagine per la gestione delle presenze.
  -

AttendanceRecord.tsx

: Pagina per registrare le presenze.

-

AttendanceReport.tsx

: Pagina per visualizzare i report delle presenze.

- **auth**: Pagine di autenticazione.
  -

Login.tsx

: Pagina di login.

-

Register.tsx

: Pagina di registrazione.

- **courses**: Pagine per la gestione dei corsi.
  -

CourseCreate.tsx

: Pagina per creare un nuovo corso.

-

CourseDetails.tsx

: Pagina per visualizzare i dettagli di un corso.

-

CourseEdit.tsx

: Pagina per modificare un corso.

-

CoursesList.tsx

: Pagina per visualizzare la lista dei corsi.

- **dashboard**: Pagine della dashboard.
  -

AdminDashboard.tsx

: Dashboard per gli amministratori.

-

StudentDashboard.tsx

: Dashboard per gli studenti.

-

TeacherDashboard.tsx

: Dashboard per gli insegnanti.

- **lessons**: Pagine per la gestione delle lezioni.
  -

LessonDetails.tsx

: Pagina per visualizzare i dettagli di una lezione.

-

LessonsList.tsx

: Pagina per visualizzare la lista delle lezioni.

-

TodayLessons.tsx

: Pagina per visualizzare le lezioni di oggi.

- **materials**: Pagine per la gestione dei materiali didattici.
  -

MaterialUpload.tsx

: Pagina per caricare nuovi materiali.

-

MaterialsList.tsx

: Pagina per visualizzare la lista dei materiali.

- **notifications**: Pagine per la gestione delle notifiche.
  -

CreateNotification.tsx

: Pagina per creare una nuova notifica.

-

NotificationCenter.tsx

: Centro notifiche.

- **quiz**: Pagine per la gestione dei quiz.
  -

QuizAttempt.tsx

: Pagina per tentare un quiz.

-

QuizCreate.tsx

: Pagina per creare un nuovo quiz.

-

QuizList.tsx

: Pagina per visualizzare la lista dei quiz.

-

QuizResults.tsx

: Pagina per visualizzare i risultati dei quiz.

- **statistics**: Pagine per visualizzare le statistiche.
  -

CourseStats.tsx

: Statistiche dei corsi.

-

StudentStats.tsx

: Statistiche degli studenti.

- **subjects**: Pagine per la gestione delle materie.
  -

SubjectList.tsx

: Pagina per visualizzare la lista delle materie.

## API

Le API sono definite nel file

apiService.ts

. Ecco alcune delle funzioni principali:

-

fetchCourses(role: string, userId: string)

: Recupera la lista dei corsi.

-

fetchCourseDetails(courseId: string)

: Recupera i dettagli di un corso.

-

fetchCourseStudents(courseId: string)

: Recupera la lista degli studenti di un corso.

-

fetchCourseTeachers(courseId: string)

: Recupera la lista dei docenti di un corso.

-

createCourse(courseData: Partial<ICourse>)

: Crea un nuovo corso.

-

fetchLessons(userId: string, role: string)

: Recupera la lista delle lezioni.

-

createLesson(lessonData: Partial<ILesson>)

: Crea una nuova lezione.

-

fetchMaterials(userId: string, role: string)

: Recupera la lista dei materiali.

-

uploadMaterial(formData: FormData)

: Carica un nuovo materiale.

-

fetchQuizzes(role?: string, userId?: string)

: Recupera la lista dei quiz.

-

createQuiz(quizData: Partial<IQuiz>)

: Crea un nuovo quiz.

-

fetchNotifications()

: Recupera la lista delle notifiche.

-

createNotification(notificationData: Partial<INotification>)

: Crea una nuova notifica.

## Licenza

Questo progetto è distribuito sotto la licenza MIT.
