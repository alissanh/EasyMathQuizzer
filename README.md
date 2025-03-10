# My Personal Project: Easy Quizzer

### Math Video Game
This project is a simple math game with falling math equations,
where users need to type in the answers to prevent the equations 
from touching the ground. Inspired by the old Asteroid learning mode 
from Quizlet and the Raining Eggs project in CPSC 110, this game could be
a fun way to integrate learning and gaming. This interests me because I am
passionate about math education and the use of innovative technology in education.

### User stories:
- As a user, I want to be able to add equations and solutions to an exam class
- As a user, I want to be able to remove equations from an exam class
- As a user, I want to be able to view the possible equations
- As a user, I want to be able to view only Addition questions
- As a user, I want to be able to save my exam questions
- As a user, I want to be able to add equations to a previously saved exam

### Instructions for Grader
- You add equations by clicking the "Make Exam" button on the home screen or clicking the "View my Exam" tab on the Sidebar
- When adding equations the program will automatically add correct solutions to all equations you added
- To remove an equation from the exam, you must answer the equation correctly and the program will remove that equation for you
- You can view the exam by clicking on "Make Exam" from the Home page or "View my Exam" on the Sidebar
- You can view only equations of a certain operator by clicking on the corresponding button on the "View my Exam" page
- You can save the state of my application by clicking "Save Exam" in the "View my Exam" tab
- You can reload the state of my application by clicking "Load Exam" in the "View my Exam" tab

### Phase 4 Task 2
- Wed Apr 03 14:29:36 PDT 2024
- Equation: 1 + 1 was added.
- Wed Apr 03 14:29:40 PDT 2024
- Equation: 4 - 2 was added.
- Wed Apr 03 14:29:43 PDT 2024
- Equation: 9 / 3 was added.
- Wed Apr 03 14:29:48 PDT 2024
- Equation: 9 / 3 was removed.
- Wed Apr 03 14:29:49 PDT 2024
- Equation: 4 - 2 was removed.
- Wed Apr 03 14:29:51 PDT 2024
- Equation: 1 + 1 was removed.

### Phase 4 Task 3
If I had more time, I could have refactored a lot of the methods in GamePanel, HomeTab, and ExamTab into other classes like TopBar, BottomBar that would have helped readability of my code. The Tab interface could have had many more methods that the tabs could've of inherited that would've also helped with readability and debugging. The game mechanics inside GamePanel could've existed as a seperate class that would make sure GamePanel had only one purpose.

More refactoring and more methods could've improved the user flow of the app because at times it can be confusing to move inbetween times. Mechanisms like a restart button could be implemented to make the program longer and replayable. Methods that allows users to choose multiple saved files could be implemented to allow users to load different exams.