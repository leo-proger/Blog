document.addEventListener('DOMContentLoaded', function () {
    // Variable to store the post ID that will be deleted
    let postIdToDelete = null;

    // Capture the post ID when the delete button is clicked
    const deleteButtons = document.querySelectorAll('.delete_post_btn');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            postIdToDelete = this.getAttribute('data-post-id');
            console.log('Post selected for deletion:', postIdToDelete);
        });
    });

    // Add event listener to the final delete button in the modal
    const deletePostFinalBtn = document.getElementById('delete_post_final_btn');
    if (deletePostFinalBtn) {
        deletePostFinalBtn.addEventListener('click', function () {
                if (postIdToDelete) {
                    // Send DELETE request to the server
                    fetch(`/posts/delete/${postIdToDelete}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                        }
                    })
                        .then(response => {
                                if (response.ok) {
                                    // Remove the post from the DOM
                                    const postElement = document.getElementById('post_id_' + postIdToDelete);
                                    if (postElement) {
                                        postElement.remove();

                                        // Check if there are any posts left
                                        const remainingPosts = document.querySelectorAll('[id^="post_id_"]');
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
        ;
    }
})
;