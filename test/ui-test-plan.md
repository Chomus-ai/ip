# Aigis UI test plan

This plan describes black-box console sessions for `aigis.Aigis`. Every input
line is one command sent to a fresh process. Expected output is an exact
stdout transcript, including the startup banner and closing message.

Run the plan from the project root with:

```text
python .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md
```

- Main class: `aigis.Aigis`
- Java requirement: Java 25 (`java` and `javac`)
- Failure policy: stop immediately after the first failed test case

## Test case 1: Exit immediately

Aim: Verify that Aigis starts and closes cleanly when the user enters `bye`.

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

## Test case 2: Manage a todo task

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

## Test case 3: Reject malformed deadline and event commands

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

## Test case 4: Add deadline and event tasks

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

## Test case 5: Reject invalid task numbers

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
