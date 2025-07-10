// Delete a post
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
const csrfToken = document.querySelector('meta[name="_csrf"]').content;
const defaultHeaders = {
    "Content-Type": "application/json",
    [csrfHeader]: csrfToken
}

document.addEventListener('DOMContentLoaded', function () {
    // Variable to store the post ID that will be deleted
    let postIdToDelete = null;

    // Capture the post ID when the delete button is clicked
    const deleteButtons = document.querySelectorAll('.delete-post-btn');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            postIdToDelete = this.getAttribute('data-post-id');
            console.log('Post selected for deletion:', postIdToDelete);
        });
    });

    // Add event listener to the final delete button in the modal
    const deletePostFinalBtn = document.getElementById('delete-post-final-btn');
    if (deletePostFinalBtn) {
        deletePostFinalBtn.addEventListener('click', function () {
                if (postIdToDelete) {
                    // Send DELETE request to the server
                    fetch(`/posts/delete/${postIdToDelete}`, {
                        method: 'POST',
                        headers: defaultHeaders
                    })
                        .then(response => {
                                if (response.ok) {
                                    // Remove the post from the DOM
                                    const postElement = document.getElementById('post-id-' + postIdToDelete);
                                    if (postElement) {
                                        postElement.remove();

                                        // Check if there are any posts left
                                        const remainingPosts = document.querySelectorAll('[id^="post-id-"]');
                                        if (remainingPosts.length === 0) {
                                            // No posts left, show the empty message
                                            const wallContainer = document.querySelector('.wall.container-md');
                                            if (wallContainer) {
                                                const emptyMessage = document.createElement('div');
                                                emptyMessage.className = 'row text-center';
                                                emptyMessage.innerHTML = `
                                                    <div class="col">
                                                        <div>There is nothing :(</div>
                                                    </div>
                                                `;
                                                wallContainer.appendChild(emptyMessage);
                                            }
                                        }
                                    }

                                    // Reset the postIdToDelete
                                    postIdToDelete = null;
                                } else {
                                    console.error('Failed to delete post');
                                }
                            }
                        )
                        .catch(error => {
                            console.error('Error occurred while deleting post:', error);
                        });
                }
            }
        )
    }
})

// Like a post
const likeButtons = document.getElementsByClassName("like-btn");

for (let btn of likeButtons) {
    btn.addEventListener("click", function () {
        const postID = btn.getAttribute("data-post-id");

        fetch(`/posts/like/${postID}`, {
            method: "POST",
            headers: defaultHeaders
        }).then(r => {
            if (!r.ok) {
                throw new Error("Error occurred on liking the post")
            }
            return r.json();
        }).then(data => {
            const action = data["Action"].toLowerCase();
            const likesCount = parseInt(btn.querySelector(".likes-count").textContent)

            if (action === "add") {
                btn.querySelector(".like-icon").setAttribute("class", "like-icon fa-solid fa-heart");
                btn.querySelector(".likes-count").textContent = "" + (likesCount + 1);
            } else {
                btn.querySelector(".like-icon").setAttribute("class", "like-icon fa-regular fa-heart");
                btn.querySelector(".likes-count").textContent = "" + (likesCount - 1);
            }
        })
    });
}

// Send comment button
const sendCommentForm = document.getElementsByClassName("send-comment-form")

for (let formElement of sendCommentForm) {
    formElement.addEventListener("submit", e => {
        e.preventDefault()
        const form = e.target

        fetch(form.action, {
            method: "POST",
            headers: {
                [csrfHeader]: csrfToken
            },
            body: new FormData(form)
        }).then(r => {
            if (!r.ok) {
                return r.json().then(errors => {
                    const message = Object.values(errors)[0]
                    alert(message)
                });
            }
            return r.json();
        }).then(data => {
            const commentText = data["Comment"]["text"]
            const commentPostID = data["Comment"]["postID"]
            const commentAuthorName = data["Comment"]["authorName"]

            // Add the new comment to the list
            addCommentToList(commentText, commentPostID, commentAuthorName);

            // Update counter
            updateCommentsCount(commentPostID);

            // Empty the form
            form.text.value = '';

            // Hide "There's nothing"
            hideEmptyMessage(commentPostID);
        })
    })
}

function addCommentToList(commentText, postID, authorName) {
    const modalBody = document.querySelector(`#comments-modal-${postID} .modal-body`);

    // Get current time
    const now = new Date();
    const timeStr = now.toLocaleDateString('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });

    const commentHTML = `
        <div class="row">
            <div class="col-12">
                <div class="comment-info row">
                    <div class="comment-author-name col">
                        <span><b>@${authorName} (you)</b></span>
                    </div>
                    <div class="comment-creation-time col text-end text-muted">
                        <span>${timeStr}</span>
                    </div>
                </div>
                <div class="comment-text row">
                    <p>${commentText}</p>
                </div>
            </div>
        </div>
    `;

    // Add the comment in the beginning
    const firstComment = modalBody.querySelector('.row:not(.text-center)');
    if (firstComment) {
        firstComment.insertAdjacentHTML('beforebegin', commentHTML);
    } else {
        modalBody.insertAdjacentHTML('beforeend', commentHTML);
    }
}

function updateCommentsCount(postID) {
    const commentsBtn = document.querySelector(`button[data-bs-target="#comments-modal-${postID}"]`);
    const countSpan = commentsBtn.querySelector('.comments-count');
    const icon = commentsBtn.querySelector('i');

    let currentCount = parseInt(countSpan.textContent);
    currentCount++;
    countSpan.textContent = currentCount;

    // Change the comment icon from empty to filled
    if (currentCount === 1) {
        icon.className = 'fa-solid fa-comment';
    }
}

function hideEmptyMessage(postID) {
    const modalBody = document.querySelector(`#comments-modal-${postID} .modal-body`);
    const emptyMessage = modalBody.querySelector('.text-center');

    if (emptyMessage && emptyMessage.textContent.includes("There's nothing")) {
        emptyMessage.style.display = 'none';
    }
}

// Message box
document.addEventListener("DOMContentLoaded", function () {
    const textarea = document.getElementById("comment-text");

    textarea.addEventListener("input", function () {
        textarea.style.height = "auto"; // Reset height
        textarea.style.height = Math.min(textarea.scrollHeight, 150) + "px";
    });
});