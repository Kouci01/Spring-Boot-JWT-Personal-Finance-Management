const goalsTableBody = document.querySelector('#goals-table tbody');
const addGoalButton = document.getElementById('add-goal');
const goalNameInput = document.getElementById('goal-name');
const goalAmountInput = document.getElementById('goal-amount');
const targetDateInput = document.getElementById('target-date');
const token = sessionStorage.getItem("jwt")

if(token === null){
    window.location.href = "login.html";
}

let goals = [];

async function renderGoals() {
    goalsTableBody.innerHTML = '';

    const response = await fetch("http://localhost:8080/api/finance/goals", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        mode: "cors"
    });

    if(response.status === 403){
        alert("Token expired, please log in again");
        sessionStorage.removeItem("jwt");
        window.location.href = "login.html"
        return;
    }

    const data = await response.json();

    data.forEach((goal, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
                    <td>${goal.goalName}</td>
                    <td>${goal.status}</td>
                    <td>IDR ${goal.targetAmount}</td>
                    <td>IDR ${goal.currentAmount}</td>
                    <td>${goal.startDate}</td>
                    <td>${goal.endDate}</td>
                    <td>
                        <button onclick="editGoal(${goal.goalId})">Edit</button>
                        <button onclick="deleteGoal(${goal.goalId})">Delete</button>
                    </td>
                `;
        goalsTableBody.appendChild(row);
    });
}

async function addGoal() {
    const name = goalNameInput.value.trim();
    const amount = parseFloat(goalAmountInput.value.trim());
    const date = targetDateInput.value;

    if (name && !isNaN(amount) && amount > 0 && date) {
        const response = await fetch("http://localhost:8080/api/finance/goals", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                goalName: name,
                targetAmount: amount,
                endDate: date
            }),
            mode: "cors"
        });

        // goals.push({ name, amount, date });
        goalNameInput.value = '';
        goalAmountInput.value = '';
        targetDateInput.value = '';
        renderGoals();
    } else {
        alert('Please enter valid goal details.');
    }
}

function editGoal(goalId) {
    alert("Goal Id: Update "+ goalId);

    // const newName = prompt('Edit goal name:', goal.name);
    // const newAmount = parseFloat(prompt('Edit goal amount:', goal.amount));
    // const newDate = prompt('Edit target date:', goal.date);
    //
    // if (newName && !isNaN(newAmount) && newAmount > 0 && newDate) {
    //     goals[index] = { name: newName, amount: newAmount, date: newDate };
    //     renderGoals();
    // } else {
    //     alert('Invalid input. No changes made.');
    // }
}

function deleteGoal(goalId) {
    // if (confirm('Are you sure you want to delete this goal?')) {
    //     goals.splice(index, 1);
    //     renderGoals();
    // }
    alert("Goal Id: Delete "+ goalId);
}

addGoalButton.addEventListener('click', addGoal);

renderGoals();