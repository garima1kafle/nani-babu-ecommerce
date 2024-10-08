document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search');
    const searchButton = document.querySelector('.search-login button');
    const suggestionsBox = document.getElementById('search-suggestions');
    const products = document.querySelectorAll('.product');

    const productNames = Array.from(products).map(product => {
        return {
            name: product.querySelector('h3').textContent,
            link: product.querySelector('a').href
        };
    });

    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase();
        showSuggestions(query);
    });

    searchButton.addEventListener('click', (event) => {
        event.preventDefault();
        const query = searchInput.value.toLowerCase();
        filterProducts(query);
    });

    document.addEventListener('click', (event) => {
        if (!suggestionsBox.contains(event.target) && event.target !== searchInput) {
            suggestionsBox.style.display = 'none';
        }
    });

    function showSuggestions(query) {
        suggestionsBox.innerHTML = '';
        if (query.length === 0) {
            suggestionsBox.style.display = 'none';
            return;
        }

        const matches = productNames.filter(product => product.name.toLowerCase().includes(query));
        matches.forEach(match => {
            const suggestion = document.createElement('a');
            suggestion.href = match.link;
            suggestion.textContent = match.name;
            suggestionsBox.appendChild(suggestion);
        });

        suggestionsBox.style.display = matches.length > 0 ? 'block' : 'none';
    }

    function filterProducts(query) {
        products.forEach(product => {
            const productName = product.querySelector('h3').textContent.toLowerCase();
            if (productName.includes(query)) {
                product.style.display = 'block';
            } else {
                product.style.display = 'none';
            }
        });
    }
});
