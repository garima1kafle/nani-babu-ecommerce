// Function to check if the ad has been closed in this session
function hasAdBeenClosed() {
    return sessionStorage.getItem('adClosed') === 'true';
}

// Function to close the ad and mark it as closed in the session storage
function closeAd() {
    document.getElementById('ad-popup').style.display = 'none';
    sessionStorage.setItem('adClosed', 'true');
}

// Function to show the ad if it has not been closed yet
function showAdIfNeeded() {
    if (!hasAdBeenClosed()) {
        document.getElementById('ad-popup').style.display = 'block';
    }
}

// Attach event listener to close button
document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('close-ad-btn').addEventListener('click', closeAd);
    showAdIfNeeded();
});
