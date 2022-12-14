package be.utopia.library.events;

public class ApplicationEvent<T> {
    public T data; 
    public String eventType;

    /**
     * Static factory method for creating events
     * @param <T>
     * Inferred from data param
     * @param data
     * Usually either DTO or id to broadcast
     * @param eventType
     * Name of the event in past simple tense (Registered, terminated ...)
     * @return
     * Returns an ApplicationEvent of type T
     */
    public static <T> ApplicationEvent<T> create(T data, String eventType){
        ApplicationEvent<T> result = new ApplicationEvent<T>();
        result.data = data;
        result.eventType = eventType;
        return result;
    }
}
