const goalsTableBody = document.querySelector('#goals-table tbody');
const addGoalButton = document.getElementById('add-goal');
const goalNameInput = document.getElementById('goal-name');
const goalAmountInput = document.getElementById('goal-amount');
const targetDateInput = document.getElementById('target-date');
const token = sessionStorage.getItem("jwt")

if (token === null) {
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

    if (response.status === 403) {
        alert("Token expired, please log in again");
        sessionStorage.removeItem("jwt");
        window.location.href = "login.html";
        return;
    }

    const data = await response.json();

    data.forEach((goal) => {
        const row = document.createElement('tr');
        row.innerHTML = `
        <td>${goal.goalName}</td>
        <td>${goal.status}</td>
        <td>IDR ${goal.targetAmount}</td>
        <td>IDR ${goal.currentAmount}</td>
        <td>${goal.startDate}</td>
        <td>${goal.endDate}</td>
        <td>
            ${goal.status !== 'Completed' ? `<button onclick="openEditPopup(${goal.goalId}, '${goal.goalName}', '${goal.status}', ${goal.targetAmount}, '${goal.endDate}')">Edit</button>` : ''}
            <button onclick="deleteGoal(${goal.goalId})">Delete</button>
        </td>
    `;
        goalsTableBody.appendChild(row);
    });

}

function openEditPopup(goalId, goalName, status, goalAmount, endDate) {
    const popupHtml = `
        <div id="edit-popup" class="popup">
            <div class="popup-content">
                <h2>Edit Goal</h2>
                <label>Goal Name</label>
                <input type="text" id="edit-goal-name" value="${goalName}">
                <label>Goal Status</label>
                <select id="edit-goal-status">
                    <option value="In Progress" ${status === 'In Progress' ? 'selected' : ''}>In Progress</option>
                    <option value="Completed" ${status === 'Completed' ? 'selected' : ''}>Completed</option>
                    <option value="Cancelled" ${status === 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                </select>
                <label>Goal Amount</label>
                <input type="number" id="edit-goal-amount" value="${goalAmount}">
                <label>Target End Date</label>
                <input type="date" id="edit-target-date" value="${endDate}">
                <div class="popup-submit">
                    <button onclick="updateGoal(${goalId})">Save</button>
                    <button onclick="closeEditPopup()">Cancel</button>
                </div>
            </div>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', popupHtml);
}

function closeEditPopup() {
    document.getElementById('edit-popup').remove();
}

async function updateGoal(goalId) {
    const updatedName = document.getElementById('edit-goal-name').value.trim();
    const updatedStatus = document.getElementById('edit-goal-status').value;
    const updatedAmount = parseFloat(document.getElementById('edit-goal-amount').value.trim());
    const updatedDate = document.getElementById('edit-target-date').value;

    if (updatedName && !isNaN(updatedAmount) && updatedAmount > 0 && updatedDate) {
        await fetch("http://localhost:8080/api/finance/goals", {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                goalId: goalId,
                goalName: updatedName,
                status: updatedStatus,
                targetAmount: updatedAmount,
                endDate: updatedDate
            }),
            mode: "cors"
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(err => { throw err });
                }
            })
            .catch(error => alert(error));

        closeEditPopup();
        renderGoals();
    } else {
        alert('Please enter valid goal details.');
    }
}

async function addGoal() {
    const name = goalNameInput.value.trim();
    const amount = parseFloat(goalAmountInput.value.trim());
    const date = targetDateInput.value;

    if (name && !isNaN(amount) && amount > 0 && date) {
        await fetch("http://localhost:8080/api/finance/goals", {
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
        })
            .then(response => {
                if(!response.ok){
                    return response.text().then(err => { throw err });
                }
            })
            .catch(error => alert(error));

        goalNameInput.value = '';
        goalAmountInput.value = '';
        targetDateInput.value = '';
        renderGoals();
        // goals.push({ name, amount, date });

    } else {
        alert('Please enter valid goal details.');
    }
}

async function deleteGoal(goalId) {
    await fetch("http://localhost:8080/api/finance/goals", {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            goalId: goalId,
            status: 'Cancelled',
        }),
        mode: "cors"
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(err => { throw err });
            }
        })
        .catch(error => alert(error));

    renderGoals();
}

addGoalButton.addEventListener('click', addGoal);
renderGoals();
