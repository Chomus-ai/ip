# Aigis UI test plan

This plan describes black-box console sessions for `aigis.Aigis`. Every input
line is one command sent to a fresh process with an isolated working directory.
Expected output is an exact stdout transcript, including the startup banner and
closing message.

Run the plan from the project root with:

```text
python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md
```

- Main class: `aigis.Aigis`
- Java requirement: Java 25 (`java` and `javac`)
- Failure policy: stop immediately after the first failed test case

## Test case 1: Exit immediately

Aim: Verify that Aigis starts with no data file and closes cleanly when the user enters `bye`.

Inputs:

```text
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 7: Reject empty fields without changing task count

Aim: Verify that empty task fields are rejected and valid tasks remain correctly positioned after invalid commands.

Inputs:

```text
list
todo   
todo valid todo
list
deadline /by Friday
deadline submit report /by Friday
list
event /from 9am /to 10am
event team meeting /from 9am /to 10am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

Task description cannot be null or blank.
New objective: valid todo
1. [T] [ ] valid todo
Please use: deadline <description> /by <date>
New objective: submit report ( by: Friday )
1. [T] [ ] valid todo
2. [D] [ ] submit report ( by: Friday )
Please use: event <description> /from <start> /to <end>
New objective: team meeting( from: 9am to: 10am )
1. [T] [ ] valid todo
2. [D] [ ] submit report ( by: Friday )
3. [E] [ ] team meeting( from: 9am to: 10am )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 2: Handle an unknown command

Aim: Verify that an unrecognized command receives an explicit response and does not create a task.

Inputs:

```text
what
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

I don't understand that command.
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 3: Manage a todo task

Aim: Verify adding a todo, listing it, marking it done, and unmarking it.

Inputs:

```text
todo read chapter
list
mark 1
list
unmark 1
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

New objective: read chapter
1. [T] [ ] read chapter
Marked as done: [T] [X] read chapter
1. [T] [X] read chapter
Unmarked as done: [T] [ ] read chapter
1. [T] [ ] read chapter
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 4: Reject malformed deadline and event commands

Aim: Verify that incomplete deadline and event commands are rejected without throwing an exception or adding a task.

Inputs:

```text
deadline report
deadline report /by
event team meeting
event team meeting /from 10am
event team meeting /from 10am /to
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

Please use: deadline <description> /by <date>
Please use: deadline <description> /by <date>
Please use: event <description> /from <start> /to <end>
Please use: event <description> /from <start> /to <end>
Please use: event <description> /from <start> /to <end>
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 5: Add deadline and event tasks

Aim: Verify that deadline and event commands parse their fields and display them in a list.

Inputs:

```text
deadline submit report /by Friday
event team meeting /from 10am /to 11am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

New objective: submit report ( by: Friday )
New objective: team meeting( from: 10am to: 11am )
1. [D] [ ] submit report ( by: Friday )
2. [E] [ ] team meeting( from: 10am to: 11am )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 6: Reject invalid task numbers

Aim: Verify that invalid and out-of-range task numbers produce clear errors without changing the task list.

Inputs:

```text
todo task
mark nope
unmark 2
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

New objective: task
Please provide a valid task number.
That task does not exist.
1. [T] [ ] task
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 8: Preserve completion state after invalid status commands

Aim: Verify that malformed and out-of-range mark commands do not alter valid task completion states or task counts.

Inputs:

```text
todo first task
mark 1
mark 1 extra
todo second task
unmark 1
mark 0
list
deadline submit report /by tomorrow
mark 3
unmark nope
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

New objective: first task
Marked as done: [T] [X] first task
Please provide a valid task number.
New objective: second task
Unmarked as done: [T] [ ] first task
That task does not exist.
1. [T] [ ] first task
2. [T] [ ] second task
New objective: submit report ( by: tomorrow )
Marked as done: [D] [X] submit report ( by: tomorrow )
Please provide a valid task number.
1. [T] [ ] first task
2. [T] [ ] second task
3. [D] [X] submit report ( by: tomorrow )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 9: Reject event markers in the wrong order

Aim: Verify that reversed event markers are rejected without displacing valid tasks or altering completion state.

Inputs:

```text
event planning /to 11am /from 10am
deadline report /by
todo retained task
deadline submit report /by Friday
event team meeting /from 10am /to 11am
list
mark 2
event planning /to 11am /from 10am
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

Please use: event <description> /from <start> /to <end>
Please use: deadline <description> /by <date>
New objective: retained task
New objective: submit report ( by: Friday )
New objective: team meeting( from: 10am to: 11am )
1. [T] [ ] retained task
2. [D] [ ] submit report ( by: Friday )
3. [E] [ ] team meeting( from: 10am to: 11am )
Marked as done: [D] [X] submit report ( by: Friday )
Please use: event <description> /from <start> /to <end>
1. [T] [ ] retained task
2. [D] [X] submit report ( by: Friday )
3. [E] [ ] team meeting( from: 10am to: 11am )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 12: Skip malformed saved records

Aim: Verify that malformed records do not crash startup or prevent later valid records from loading.

Initial data:

```text
T / 1 / read / book
not a task record
D / 2 / invalid status
E / 0 / incomplete event (10am)
X / 0 / unknown type
D / 0 / send report  (Friday)
```

Inputs:

```text
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

1. [T] [X] read / book
2. [D] [ ] send report ( by: Friday )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 13: Reject repeated task markers

Aim: Verify that repeated deadline or event markers are rejected without adding malformed tasks.

Inputs:

```text
deadline report /by Friday /by Monday
event meeting /from 10am /to 11am /to 12pm
todo retained task
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

Please use: deadline <description> /by <date>
Please use: event <description> /from <start> /to <end>
New objective: retained task
1. [T] [ ] retained task
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 11: Load tasks from disk

Aim: Verify that saved todos, deadlines, and events are loaded with their completion state.

Initial data:

```text
T / 1 / read book
D / 0 / return book  (June 6th)
E / 0 / project meeting (Aug 6th 2-4pm)
```

Inputs:

```text
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

1. [T] [X] read book
2. [D] [ ] return book ( by: June 6th )
3. [E] [ ] project meeting( from: Aug 6th to: 2-4pm )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```

## Test case 10: Save changed tasks to disk

Aim: Verify that successful additions and status changes are handled in one session for persistence.

Inputs:

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th /to 2-4pm
mark 1
list
bye
```

Expected output:

```text
      __        __     _______   __      ________  
     /""\      |" \   /" _   "| |" \    /"       ) 
    /    \     ||  | (: ( \___) ||  |  (:   \___/  
   /' /\  \    |:  |  \/ \      |:  |   \___  \    
  //  __'  \   |.  |  //  \ ___ |.  |    __/  \\   
 /   /  \\  \  /\  |\(:   _(  _|/\  |\  /" \   :)  
(___/    \___)(__\_|_)\_______)(__\_|_)(_______/   

Aigis is ready to help!
Awaiting commands...

New objective: read book
New objective: return book ( by: June 6th )
New objective: project meeting( from: Aug 6th to: 2-4pm )
Marked as done: [T] [X] read book
1. [T] [X] read book
2. [D] [ ] return book ( by: June 6th )
3. [E] [ ] project meeting( from: Aug 6th to: 2-4pm )
 ----------------------------------------------------
Tasks completed. See you again soon!
__________________________________________________________

```
