```java

public class Counter extends UIComponent {
    private final State<Integer> counterState = new State<>(0);
    
    @Override
    public void render(Renderer renderer) {
        renderer.setItem(renderer.nextSlot(), ctx -> {
           int counter = ctx.getState(counterState);                // Notify renderer to regenerate item on state change
           return ItemStack.builder(Material.OAK_BUTTON)
                   .name(Component.text("Counter: " + counter))
                   .build();
        });
    }
}

UI ui = new UI(Component.text("Cool UI"), 6)
        .addComponent(new Counter());

```
